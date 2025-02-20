<?php

namespace App\Controller\API;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use App\Entity\User;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Serializer\SerializerInterface;

class APIUserController extends AbstractController
{
  #[Route('/api/register', methods :["POST"])]
  public function createUser(
      Request $request, 
      EntityManagerInterface $entityManager,
      SerializerInterface $serializer
  ) : JsonResponse
  {
      $data = $request->getContent();
      try {
          $user = $serializer->deserialize($data, User::class, 'json');
          $duplicateUser = $entityManager->getRepository(User::class)->findOneBy(['email' => $user->getEmail()]);
          if($duplicateUser){
            return new JsonResponse(["message" => "Cet email est déjà utilisé."], Response::HTTP_BAD_REQUEST);
          }

          $user->setCreatedAt(new \DateTimeImmutable());
          $user->setRoles(["ROLE_PARTICIPANT"]);

          $jwt = $entityManager->getRepository(User::class)->registerUser($user);

          if(!$jwt)
          {
            return new JsonResponse(["message" => "error token"], Response::HTTP_BAD_REQUEST);
          }

          return new JsonResponse(["message" => "Création de compte réussit", "token" => $jwt ], Response::HTTP_OK);
      } 
      catch (\Exception $e) {
            echo $e->getMessage();
            return new JsonResponse(["message" => "Error creation user"], Response::HTTP_BAD_REQUEST);
      }
  }

  #[Route('/api/login', methods :["POST"])]
  public function login(
      Request $request, 
      EntityManagerInterface $entityManager,
      SerializerInterface $serializer
  ) : JsonResponse
  {
      $data = $request->getContent();
      try {
          $user = $serializer->deserialize($data, User::class, 'json');
          $duplicateUser = $entityManager->getRepository(User::class)->findOneBy(['email' => $user->getEmail()]);

          if(!$duplicateUser){
            return new JsonResponse(["message" => "Mot de passe ou email incorrect"], Response::HTTP_BAD_REQUEST);
          }
          $user->setCreatedAt(new \DateTimeImmutable());

          $jwt = $entityManager->getRepository(User::class)->authenticateUser($duplicateUser, $user->getPassword());
          if($jwt == null)
          {
            return new JsonResponse(["message" => "Mot de passe ou email incorrect"], Response::HTTP_BAD_REQUEST);
          }
          return new JsonResponse(["message" => "Connexion réussit", "token" => $jwt ], Response::HTTP_OK);
      } 
      catch (\Exception $e) {
            echo $e->getMessage();
            return new JsonResponse(["message" => "Error login user."], Response::HTTP_BAD_REQUEST);
      }
  }
}
