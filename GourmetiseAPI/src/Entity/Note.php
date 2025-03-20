<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Note
{
    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Evaluation::class, inversedBy: 'criteria')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Evaluation $evaluation = null;

    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Criteria::class)]
    #[ORM\JoinColumn(nullable: false)]
    private ?Criteria $criteria = null;

    #[ORM\Column(type: 'integer')]
    private int $value;

    public function __construct(Evaluation $evaluation, Criteria $criteria, int $value)
    {
        $this->evaluation = $evaluation;
        $this->criteria = $criteria;
        $this->value = $value;
    }

    // Getter et setter pour $evaluation, $criteria, et $value
    public function getEvaluation(): ?Evaluation
    {
        return $this->evaluation;
    }

    public function setEvaluation(?Evaluation $evaluation): self
    {
        $this->evaluation = $evaluation;

        return $this;
    }

    public function getCriteria(): ?Criteria
    {
        return $this->criteria;
    }

    public function setCriteria(Criteria $criteria): self
    {
        $this->criteria = $criteria;

        return $this;
    }

    public function getValue(): int
    {
        return $this->value;
    }

    public function setValue(int $value): self
    {
        $this->value = $value;

        return $this;
    }
}
