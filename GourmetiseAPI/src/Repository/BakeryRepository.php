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
     * Récupère le top 3 des boulangeries basées sur la moyenne des scores des évaluations.
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
                AVG(subquery.total_score) AS average_score
            FROM 
                bakery
            JOIN 
                evaluation ON bakery.siret = evaluation.bakery_siret
            JOIN 
                (SELECT 
                    evaluation_id,
                    SUM(note.value) AS total_score
                FROM 
                    note
                GROUP BY 
                    note.evaluation_id) AS subquery ON evaluation.id = subquery.evaluation_id
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
