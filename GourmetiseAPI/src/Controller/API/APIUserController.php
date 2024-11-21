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
  #[Route('/api/users', methods :["POST"])]
  public function createUser(
      Request $request, 
      EntityManagerInterface $entityManager,
      SerializerInterface $serializer
  ) : JsonResponse
  {
      $data = $request->getContent();
      try {
          $user = $serializer->deserialize($data, User::class, 'json');
          $user->setCreatedAt(new \DateTimeImmutable());

          $entityManager->persist($user);
          $entityManager->flush();

          return $this->json('User created', Response::HTTP_CREATED);
      } 
      catch (\Exception $e) {
            echo $e->getMessage();
          return $this->json ('Error creation user', Response::HTTP_BAD_REQUEST);
      }
  }
}
