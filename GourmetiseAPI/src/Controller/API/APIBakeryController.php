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
            if($user->getRole() !== "participant")
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
           // echo "Message d'erreur: " . $e->getMessage();  

            if (preg_match('/Duplicate entry.*for key \'bakery.PRIMARY\'/', $e->getMessage())) {
                // Le SIRET est dupliqué
                return new JsonResponse(["message" => "Numéro de siret déjà existant, cette boulangerie est déjà inscrite."], Response::HTTP_CONFLICT);
            } elseif (preg_match('/Duplicate entry.*for key \'bakery.UNIQ/', $e->getMessage())) {
                // L'email est dupliqué
                return new JsonResponse(["message" => "Vous avez déjà inscrit une boulangerie avec ce compte."], Response::HTTP_CONFLICT);
            }

            // Si une violation de contrainte d'unicité inconnue se produit
            return new JsonResponse(["message" => "Cette boulangerie est déjà inscrite ou vous avez déjà inscrite une boulangerie avec ce compte."], Response::HTTP_CONFLICT);
        } 
        catch (\Exception $e) {
            //toutes les autres types d'erreurs
            echo $e->getMessage();
            return new JsonResponse(["message" => "Erreur lors de l\'inscription de la boulangerie."], Response::HTTP_CONFLICT);
        }
    }
}
