<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;

class AuthController extends AbstractController
{
    #[Route('api/login_check', name: 'api_login_check', methods: ['POST'])]
    public function login_check()
    {

    }
}