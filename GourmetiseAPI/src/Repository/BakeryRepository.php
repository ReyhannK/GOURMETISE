<?php

namespace App\Repository;

use App\Entity\Bakery;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Bakery>
 */
class BakeryRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Bakery::class);
    }

    /**
     * Récupère le classement des boulangeries basées sur la moyenne des scores des évaluations.
     *
     * @return array
     */
    public function getRanking($limit = null)
    {
        $limitClause = '';
        if ($limit !== null) {
            $limitClause = "LIMIT $limit";
        }   

        $sql = "
            SELECT 
                bakery.siret AS siret, 
                bakery.name AS name,
                AVG(total_scores.total_score) AS average_score
            FROM 
                bakery
            JOIN 
                evaluation ON bakery.siret = evaluation.bakery_siret
            JOIN 
                total_scores ON evaluation.id = total_scores.evaluation_id
            GROUP BY 
                bakery.siret
            ORDER BY 
                average_score DESC
            $limitClause;
        ";

        $connection = $this->getEntityManager()->getConnection();

        $stmt = $connection->executeQuery($sql);
    
        return $stmt->fetchAllAssociative();
    }

    /**
     * Récupère le rang et score moyen d'une boulangerie
     *
     * @return array
     */
    public function getMyRank(Bakery $Bakery)
    {
        $siret = $Bakery->getSiret();

        $sql = "
        SELECT 
            bakery.siret,
            bakery.name,
            AVG(total_scores.total_score) AS average_score,
            RANK() OVER (ORDER BY AVG(total_scores.total_score) DESC) AS rank_value
        FROM 
            bakery
        JOIN 
            evaluation ON bakery.siret = evaluation.bakery_siret
        JOIN 
            total_scores ON evaluation.id = total_scores.evaluation_id
        GROUP BY 
            bakery.siret, bakery.name
        ";

        $connection = $this->getEntityManager()->getConnection();

        $stmt = $connection->executeQuery($sql);
        $allRankings = $stmt->fetchAllAssociative();

        $myRank = null;
        foreach ($allRankings as $ranking) {
            if ($ranking['siret'] === $siret) {
                $myRank = $ranking;
                break;
            }
        }
    
        return $myRank;
    }

    //    /**
    //     * @return Bakery[] Returns an array of Bakery objects
    //     */
    //    public function findByExampleField($value): array
    //    {
    //        return $this->createQueryBuilder('b')
    //            ->andWhere('b.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->orderBy('b.id', 'ASC')
    //            ->setMaxResults(10)
    //            ->getQuery()
    //            ->getResult()
    //        ;
    //    }

    //    public function findOneBySomeField($value): ?Bakery
    //    {
    //        return $this->createQueryBuilder('b')
    //            ->andWhere('b.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->getQuery()
    //            ->getOneOrNullResult()
    //        ;
    //    }
}
