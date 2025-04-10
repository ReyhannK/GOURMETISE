<?php

namespace App\Controller\API;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use App\Entity\ContestParams;
use App\Repository\ContestParamsRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Serializer\SerializerInterface;

class APIContestParamsController extends AbstractController
{
  #[Route('/api/contestParams', methods :["GET"])]
  public function getContestParams(ContestParamsRepository $repository) : JsonResponse
  {
      $contestParams = $repository->findLastContestParams();
      if(!$contestParams)
      {
        return new JsonResponse(["message" => "Erreur lors de la récupération des paramètres du concours."], Response::HTTP_BAD_REQUEST);
      }
      return $this->json( ['contestParams' => $contestParams], Response::HTTP_OK, [], ['groups' => ['ContestParams:Read']]);
  }

  #[Route('/api/contestParams', methods :["POST"])]
  public function createContestParams(
      Request $request, 
      EntityManagerInterface $entityManager,
      SerializerInterface $serializer
  ) : JsonResponse
  {
      $data = $request->getContent();
      try {
          $contestParams = $serializer->deserialize($data, ContestParams::class, 'json', ['groups' => 'ContestParams:Write']);
          $entityManager->persist($contestParams);
          $entityManager->flush();

          return $this->json('ContestParams created', Response::HTTP_CREATED);
      } 
      catch (\Exception $e) {
          return $this->json ('ContestParams already exist', Response::HTTP_BAD_REQUEST);
      }
  }

  #[Route('/api/updateContestParams', methods :["PUT", "PATCH"])]
    public function updateContestParams(
        Request $request, 
        EntityManagerInterface $entityManager,
        SerializerInterface $serializer
    ) : JsonResponse
    {
        $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
        if (!$contestParams) {
            return new JsonResponse(["message" => "Paramètres du concours n'existent pas."], Response::HTTP_NOT_FOUND);
        }
        if((!in_array('ROLE_GERANT', $this->getUser()->getRoles()))){
            return new JsonResponse(["message" => "Vous n'etes pas autorisé à modifier les paramètres de concours."], Response::HTTP_FORBIDDEN );
        }
        $data = $request->getContent();
        try {
            $serializer->deserialize($data, ContestParams::class, 'json', 
                                     ['object_to_populate' => $contestParams, 'groups' => 'ContestParams:Write']);
            $entityManager->flush();
            return new JsonResponse(["message" => "Paramètres du concours mis à jour."], Response::HTTP_OK);
        }
        catch (\Exception $e) {
            return new JsonResponse(['error' => $e->getMessage()], 
                                     Response::HTTP_BAD_REQUEST);
        }
    }

  #[Route('/api/contestParams', methods :["DELETE"])]
    public function deleteContestParams(EntityManagerInterface $entityManager) : JsonResponse
    {
        $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
        if (!$contestParams) {
            return $this->json('ContestParams not exist', Response::HTTP_NOT_FOUND);
        }
        $entityManager->remove($contestParams);
        $entityManager->flush();
        return $this->json(null, Response::HTTP_NO_CONTENT);
    }

    #[Route('/api/contestParams/dates', methods :["PATCH"])]
    public function modifyDates(
        Request $request, 
        EntityManagerInterface $entityManager,
        SerializerInterface $serializer,
        ) : JsonResponse
    {
        $contestParams = $entityManager->getRepository(ContestParams::class)->find(1);
        if (!$contestParams) {
            return new JsonResponse(["message" => "Paramètres du concours n'existent pas."], Response::HTTP_NOT_FOUND);
        }
        if(new \DateTimeImmutable() >= $contestParams->getStartRegistration()){
            return new JsonResponse(["message" => "Les inscriptions au concours ont déjà commencé, vous ne pouvez plus les modifier."], Response::HTTP_BAD_REQUEST);
        }
        if((!in_array('ROLE_GERANT', $this->getUser()->getRoles()))){
            return new JsonResponse(["message" => "Vous n'etes pas autorisé à consulter modifier les paramètres de concours."], Response::HTTP_FORBIDDEN );
        }
        $data = $request->getContent();
        try {
            $updatedContestParams  = $serializer->deserialize($data, ContestParams::class, 'json', ['groups' => 'ContestParams:Write']);
            
            $contestParams->setStartRegistration($updatedContestParams->getStartRegistration());
            $contestParams->setEndRegistration($updatedContestParams->getEndRegistration());
            $contestParams->setStartEvaluation($updatedContestParams->getStartEvaluation());
            $contestParams->setEndEvaluation($updatedContestParams->getEndEvaluation());

            $entityManager->persist($contestParams);
            $entityManager->flush();
  
            return new JsonResponse(["message" => "Paramètres du concours mis à jour."], Response::HTTP_OK);
        } 
        catch (\Exception $e) {
            return new JsonResponse(["message" => "Erreur dans les paramètres de concours."], Response::HTTP_BAD_REQUEST);
        }
    }
}
