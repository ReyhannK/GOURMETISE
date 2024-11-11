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
      $contestParams = $repository->find(1);
      return $this->json($contestParams, Response::HTTP_OK, [], ['groups' => ['ContestParams:Read']]);
  }

  #[Route('/api/contestParams', methods :["POST"])]
  public function createContestParams(
      Request $request, 
      EntityManagerInterface $entityManager,
      SerializerInterface $serializer
  ) : JsonResponse
  {
      // récupérer le contenu JSON de la requête
      $data = $request->getContent();
      try {
          // désérialiser le JSON en une instance de l'entité ContestParams
          $contestParams = $serializer->deserialize($data, ContestParams::class, 'json', ['groups' => 'ContestParams:Write']);
          // enregistrer le ContestParams dans la base de données
          $entityManager->persist($contestParams);
          $entityManager->flush();

          // envoyer réponse de succès de la création
          return $this->json('ContestParams created', Response::HTTP_CREATED);
      } 
      catch (\Exception $e) {
          return $this->json ('ContestParams already exist', Response::HTTP_BAD_REQUEST);
      }
  }

  #[Route('/api/contestParams', methods :["PUT", "PATCH"])]
    public function updateContestParams(
        Request $request, 
        EntityManagerInterface $entityManager,
        SerializerInterface $serializer
    ) : JsonResponse
    {
        // trouver le contestParams dans la base de données
        $contestParams = $entityManager->getRepository(ContestParams::class)->find(1);
        if (!$contestParams) {
            return $this->json('ContestParams not exist', Response::HTTP_NOT_FOUND);
        }
        // récupérer le contenu JSON de la requête
        $data = $request->getContent();
        try {
            // désérialiser le JSON et mise à jour de l'entité ContestParams
            $serializer->deserialize($data, ContestParams::class, 'json', 
                                     ['object_to_populate' => $contestParams, 'groups' => 'ContestParams:Write']);
            // enregistrer le concurrent modifié dans la base de données
            $entityManager->flush();
            // envoyer réponse de succès de la mise à jour
            return $this->json( 'ContestParams updated', Response::HTTP_OK);
        }
        catch (\Exception $e) {
            return new JsonResponse(['error' => $e->getMessage()], 
                                     Response::HTTP_BAD_REQUEST);
        }
    }

  #[Route('/api/contestParams', methods :["DELETE"])]
    public function deleteContestParams(EntityManagerInterface $entityManager) : JsonResponse
    {
        // récupère l'entité à supprimer
        $contestParams = $entityManager->getRepository(ContestParams::class)->find(1);
        // vérifie si l'entité existe
        if (!$contestParams) {
            return $this->json('ContestParams not exist', Response::HTTP_NOT_FOUND);
        }
        // supprime l'entité de la base de données
        $entityManager->remove($contestParams);
        $entityManager->flush();
        // retourne une réponse HTTP 204 No Content
        return $this->json(null, Response::HTTP_NO_CONTENT);
    }
}
