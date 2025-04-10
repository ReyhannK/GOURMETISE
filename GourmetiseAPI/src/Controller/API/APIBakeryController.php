<?php

namespace App\Controller\API;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use App\Entity\Bakery;
use App\Entity\ContestParams;
use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Serializer\SerializerInterface;
use Doctrine\DBAL\Exception\UniqueConstraintViolationException;

class APIBakeryController extends AbstractController
{
    #[Route('/api/mobile/bakeries', methods :["GET"])]
    public function getContestParams(EntityManagerInterface $entityManager) : JsonResponse
    {
        try{
            $now = new \DateTimeImmutable();
            $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
            if(!$contestParams){
                return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
            }
            if($now < $contestParams->getStartEvaluation()){
                return new JsonResponse(["message" => "Les votes au concours ne sont pas encore ouverts."], Response::HTTP_BAD_REQUEST);
            }
            if($now>$contestParams->getEndEvaluation()){
                return new JsonResponse(["message" => "Les votes au concours sont fermés."], Response::HTTP_BAD_REQUEST);
            }

            $bakeries = $entityManager->getRepository(Bakery::class)->findAll();
            if(empty($bakeries)){
                return new JsonResponse(["message" => "Il n'y a aucune boulangerie inscrite"], Response::HTTP_CONFLICT);
            }
            return $this->json( ['bakeries' => $bakeries,'contestParams' => $contestParams], Response::HTTP_OK, [], ['groups' => ['Bakery:Read', 'ContestParams:Mobile']]);
        }catch(\Exception $e){
            echo $e->getMessage();
            return new JsonResponse(["message" => "Erreur lors de la récupération des boulangerie."], Response::HTTP_CONFLICT);
        }
    }

    #[Route('/api/bakeries', methods :["POST"])]
    public function createBakery(
        Request $request, 
        EntityManagerInterface $entityManager,
        SerializerInterface $serializer
    ) : JsonResponse
    {
        $data = $request->getContent();
        try {
            $now = new \DateTimeImmutable();
            $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
            if(!$contestParams){
                return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
            }
            if($now < $contestParams->getStartRegistration()){
                return new JsonResponse(["message" => "Les inscriptions aux concours ne sont pas encore ouvertes."], Response::HTTP_BAD_REQUEST);
            }
            if($now>$contestParams->getEndRegistration()){
                return new JsonResponse(["message" => "Le concours a déjà débuté, les inscriptions sont fermés."], Response::HTTP_BAD_REQUEST);
            } 
            $bakery = $serializer->deserialize($data, Bakery::class, 'json');

            if(empty($bakery->getSiret()) || empty($bakery->getName()) || empty($bakery->getStreet()) || empty($bakery->getPostalCode()) ||
                empty($bakery->getCity()) || empty($bakery->getTelephoneNumber()) || empty($bakery->getContactName())
                || empty($bakery->getBakeryDescription()) || empty($bakery->getProductsDecription())
                || empty($bakery->getUser()->getEmail()) 
            ){
                return new JsonResponse(["message" => "Les informations de la boulangerie ne sont pas complètes."], Response::HTTP_BAD_REQUEST);
            }

            $user = $entityManager->getRepository(User::class)->findOneBy(['email' => $bakery->getUser()->getEmail()]);
            if(!$user)
            {
                return new JsonResponse(["message" => "Vous n'avez pas de compte."], Response::HTTP_BAD_REQUEST);
            }
            if((!in_array('ROLE_PARTICIPANT', $user->getRoles())))
            {
                return new JsonResponse(["message" => "Vous n'etes pas un participant."], Response::HTTP_BAD_REQUEST);
            }

            $bakery->setUser($user);
            $bakery->setConsentDate(new \DateTimeImmutable());
            
            $entityManager->persist($bakery);
            $entityManager->flush();

            return new JsonResponse(["message" => "Boulangerie inscrite avec succès ! "], Response::HTTP_CREATED);
        }
        catch (UniqueConstraintViolationException $e) {

            if (preg_match('/Duplicate entry.*for key \'bakery.PRIMARY\'/', $e->getMessage())) {
                return new JsonResponse(["message" => "Numéro de siret déjà existant, cette boulangerie est déjà inscrite."], Response::HTTP_CONFLICT);
            } elseif (preg_match('/Duplicate entry.*for key \'bakery.UNIQ/', $e->getMessage())) {
                return new JsonResponse(["message" => "Vous avez déjà inscrit une boulangerie avec ce compte."], Response::HTTP_CONFLICT);
            }

            return new JsonResponse(["message" => "Cette boulangerie est déjà inscrite ou vous avez déjà inscrite une boulangerie avec ce compte."], Response::HTTP_CONFLICT);
        } 
        catch (\Exception $e) {
            //toutes les autres types d'erreurs
            echo $e->getMessage();
            return new JsonResponse(["message" => "Erreur lors de l\'inscription de la boulangerie."], Response::HTTP_CONFLICT);
        }
    }

    #[Route('/api/getFullRanking', methods :["GET"])]
    public function getFullRanking(EntityManagerInterface $entityManager) : JsonResponse
    {
      if((!in_array('ROLE_GERANT', $this->getUser()->getRoles()))){
          return new JsonResponse(["message" => "Vous n'etes pas autorisé à consulter le classement général."], Response::HTTP_FORBIDDEN );
      }
  
      $now = new \DateTimeImmutable();
      $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
      if(!$contestParams){
          return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
      }
      if($now < $contestParams->getEndEvaluation()){
          return new JsonResponse(["message" => "Les évaluations aux concours ne sont pas encore finies."], Response::HTTP_BAD_REQUEST);
      }
      
        $rankings = $entityManager->getRepository(Bakery::class)->getRanking();
        return $this->json( ['rankings' => $rankings], Response::HTTP_OK, [], ['groups' => ['Bakery:Read']]);
    }

    #[Route('/api/getRankingTop3', methods :["GET"])]
  public function getRanking(EntityManagerInterface $entityManager) : JsonResponse
  {
    $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
    if(!$contestParams){
        return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
    }
    if(!$contestParams->getPublished()){
        return new JsonResponse(["message" => "Le classement n'a pas été publié."], Response::HTTP_BAD_REQUEST);
    }
    
    $rankings = $entityManager->getRepository(Bakery::class)->getRanking(3);
    return $this->json( ['rankings' => $rankings], Response::HTTP_OK, [], ['groups' => ['Bakery:Read']]);
  }

  #[Route('/api/getMyRank', methods :["GET"])]
  public function getMyRank(EntityManagerInterface $entityManager) : JsonResponse
  {
    $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
    if(!$contestParams){
        return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
    }
    if(!$contestParams->getPublished()){
        return new JsonResponse(["message" => "Le classement n'a pas été publié."], Response::HTTP_BAD_REQUEST);
    }

    $bakery = $entityManager->getRepository(Bakery::class)->findOneBy(['user' => $this->getUser()]);
    if(!$bakery){
        return new JsonResponse(["message" => "La boulangerie associée à votre compte n'a pas été trouvé ou n'existe pas"], Response::HTTP_BAD_REQUEST);
    }
    
    $myRank = $entityManager->getRepository(Bakery::class)->getMyRank($bakery);
    return $this->json( ['myRank' => $myRank], Response::HTTP_OK, [], ['groups' => ['Bakery:Read']]);
  }
}
