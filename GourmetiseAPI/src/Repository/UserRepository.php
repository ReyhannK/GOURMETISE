<?php

namespace App\Repository;

use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

/**
 * @extends ServiceEntityRepository<User>
 */
class UserRepository extends ServiceEntityRepository
{
    private UserPasswordHasherInterface $passwordHasher;
    private JWTTokenManagerInterface $JWTManager;

    public function __construct(
        ManagerRegistry $registry,
        UserPasswordHasherInterface $passwordHasher,
        JWTTokenManagerInterface $JWTManager
        )
    {
        parent::__construct($registry, User::class);
        $this->passwordHasher = $passwordHasher;
        $this->JWTManager = $JWTManager;
    }

    public function registerUser(User $user): ?string
    {
        //Hasher le mdp
        $hashedPassword = $this->passwordHasher->hashPassword($user, $user->getPassword());
        $user->setPassword($hashedPassword);

        //Persiste l'utilisateur en bdd
        $entityManager = $this->getEntityManager();
        $entityManager->persist($user);
        $entityManager->flush();

        //Genere un JWT pour l'utilisateur
        return $this->JWTManager->create($user);
    }

    public function authenticateUser(User $user, string $plainPassword): ?string
    {
        //Verifie si le mdp correspond au mdp hashé de l'utilisateur
        if($this->passwordHasher->isPasswordValid($user, $plainPassword))
        {
            //Genere un JWT pour l'utilisateur
            return $this->JWTManager->create($user);
        }
        return null;
    }

    //    /**
    //     * @return User[] Returns an array of User objects
    //     */
    //    public function findByExampleField($value): array
    //    {
    //        return $this->createQueryBuilder('u')
    //            ->andWhere('u.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->orderBy('u.id', 'ASC')
    //            ->setMaxResults(10)
    //            ->getQuery()
    //            ->getResult()
    //        ;
    //    }

    //    public function findOneBySomeField($value): ?User
    //    {
    //        return $this->createQueryBuilder('u')
    //            ->andWhere('u.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->getQuery()
    //            ->getOneOrNullResult()
    //        ;
    //    }
}
