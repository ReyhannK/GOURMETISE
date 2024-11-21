<?php

namespace App\Controller\API;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use App\Entity\Bakery;
use App\Entity\User;
use App\Repository\BakeryRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Serializer\SerializerInterface;

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
            $bakery = $serializer->deserialize($data, Bakery::class, 'json');

            if(empty($bakery->getSiret()) || empty($bakery->getName()) || empty($bakery->getStreet()) || empty($bakery->getPostalCode()) ||
                empty($bakery->getCity()) || empty($bakery->getTelephoneNumber()) || empty($bakery->getContactName())
                || empty($bakery->getBakeryDescription()) || empty($bakery->getProductsDecription()) || empty($bakery->getConsentDate()) 
                || empty($bakery->getUser()->getEmail()) 
            ){
                return $this->json('Bakery not complet', Response::HTTP_CREATED);
            }

            $user = $entityManager->getRepository(User::class)->findOneBy(['email' => $bakery->getUser()->getEmail()]);
            if(!$user)
            {
                return $this->json('User does not exist', Response::HTTP_CREATED);
            }
            $bakery->setUser($user);
            
            $entityManager->persist($bakery);
            $entityManager->flush();

            return $this->json('Bakery created', Response::HTTP_CREATED);
        } 
        catch (\Exception $e) {
            echo $e->getMessage();
            return $this->json ('Error creation Bakery', Response::HTTP_BAD_REQUEST);
        }
    }
}
