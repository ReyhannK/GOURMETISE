<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\Response;

class ProfileUser extends AbstractController
{
    #[Route('api/profile', name: 'profile', methods: ['GET'])]
    public function getProfile(): JsonResponse
    {
        //recupere l'utilisateur connecte
        $user = $this->getUser();
        return $this->json($user, Response::HTTP_OK);
    }
}