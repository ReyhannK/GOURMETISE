<?php

namespace App\Controller\API;

use App\Entity\Bakery;
use App\Entity\Criteria;
use App\Entity\Evaluation;
use App\Entity\Note;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\ContestParams;

class APIEvaluationController extends AbstractController
{
  #[Route('/api/mobile/evaluations', methods :["POST"])]
    public function sendEvaluations(
        Request $request, 
        EntityManagerInterface $entityManager,
    ) : JsonResponse
    {
        $data = json_decode($request->getContent(), true);
        dd($data);
        try{
            if(count($data) < 5 ){
                return new JsonResponse(["message" => " Vous avez fait moins de 5 évaluations, vous ne pouvez pas demander l’envoi des évaluations ."], Response::HTTP_BAD_REQUEST);
            }
            $now = new \DateTimeImmutable();
            $contestParams = $entityManager->getRepository(ContestParams::class)->findLastContestParams();
            if(!$contestParams){
                return new JsonResponse(["message" => "Erreur dans les paramètres du concours."], Response::HTTP_BAD_REQUEST);
            }
            if($now < $contestParams->getEndEvaluation()){
                return new JsonResponse(["message" => " la date de fin des évaluations n’est pas atteinte, vous ne pouvez pas demander l’envoi de mes évaluations."], Response::HTTP_BAD_REQUEST);
            }
            foreach($data as $evaluation){
                $bakery = $entityManager->getRepository(Bakery::class)->findOneBy(['siret' => $evaluation['bakery_siret']]);

                if(!$bakery){
                    return new JsonResponse(['message' => 'Numéro de siret'.$evaluation['bakery_siret'].' non existant'], JsonResponse::HTTP_BAD_REQUEST);
                }

                $evaluationEntity = new Evaluation();
                $evaluationEntity->setCodeTicket($evaluation['code_ticket']);

                $dateTimeImmutable = \DateTimeImmutable::createFromFormat(\DateTime::ATOM, $evaluation['date_evaluation']);
                $evaluationEntity->setDateEvaluation($dateTimeImmutable);

                $bakery->addEvaluation($evaluationEntity);

                $entityManager->persist($evaluationEntity);

                foreach($evaluation['notes'] as $note){
                    $criteria = $entityManager->getRepository(Criteria::class)->findOneBy(['id' => $note['criteria_id']]);
                    if(!$criteria){
                        continue;
                    }

                    $note = new Note($evaluationEntity, $criteria, $note['value']);
                    $entityManager->persist($note);
                }
            }
            $entityManager->flush();
            return new JsonResponse(['message' => 'Evaluation créée avec succès!'], JsonResponse::HTTP_CREATED);
        }
        catch (\Exception $e) {
            echo $e->getMessage();
            return new JsonResponse(["message" => "Erreur lors de récupération des évaluations."], Response::HTTP_CONFLICT);
        }
    }
}
