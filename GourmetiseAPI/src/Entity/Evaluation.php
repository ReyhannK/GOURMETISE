<?php

namespace App\Entity;

use App\Repository\EvaluationRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: EvaluationRepository::class)]
class Evaluation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $code_ticket = null;

    #[ORM\Column]
    private ?\DateTimeImmutable $date_evaluation = null;

    #[ORM\ManyToOne(inversedBy: 'evaluations')]
    #[ORM\JoinColumn(name: 'bakery_siret', referencedColumnName: 'siret', nullable: false)]
    private ?Bakery $bakery = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCodeTicket(): ?string
    {
        return $this->code_ticket;
    }

    public function setCodeTicket(string $code_ticket): static
    {
        $this->code_ticket = $code_ticket;

        return $this;
    }

    public function getDateEvaluation(): ?\DateTimeImmutable
    {
        return $this->date_evaluation;
    }

    public function setDateEvaluation(\DateTimeImmutable $date_evaluation): static
    {
        $this->date_evaluation = $date_evaluation;

        return $this;
    }

    public function getBakery(): ?Bakery
    {
        return $this->bakery;
    }

    public function setBakery(?Bakery $bakery): static
    {
        $this->bakery = $bakery;

        return $this;
    }
}
