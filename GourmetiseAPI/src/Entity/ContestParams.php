<?php

namespace App\Entity;

use App\Repository\ContestParamsRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: ContestParamsRepository::class)]
class ContestParams
{
    #[ORM\Id]
    #[ORM\Column(type: 'integer')]
    private int $id = 1;
    
    #[ORM\Column(type: 'string', length: 255, nullable: false)]
    #[Groups(['ContestParams:Read', 'ContestParams:Write'])]
    private ?string $title = null;

    #[ORM\Column(type: 'text', nullable: false)]
    #[Groups(['ContestParams:Read', 'ContestParams:Write'])]
    private ?string $description = null;

    #[ORM\Column(type: 'datetime', nullable: false)]
    #[Groups(['ContestParams:Read', 'ContestParams:Write', 'ContestParams:Mobile'])]
    private ?\DateTimeInterface $startRegistration = null;

    #[ORM\Column(type: 'datetime', nullable: false)]
    #[Groups(['ContestParams:Read', 'ContestParams:Write', 'ContestParams:Mobile'])]
    private ?\DateTimeInterface $endRegistration = null;

    #[ORM\Column(type: 'datetime', nullable: false)]
    #[Groups(['ContestParams:Read','ContestParams:Write', 'ContestParams:Mobile'])]
    private ?\DateTimeInterface $startEvaluation = null;

    #[ORM\Column(type: 'datetime', nullable: false)]
    #[Groups(['ContestParams:Read','ContestParams:Write', 'ContestParams:Mobile'])]
    private ?\DateTimeInterface $endEvaluation = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getTitle(): ?string
    {
        return $this->title;
    }

    public function setTitle(string $title): static
    {
        $this->title = $title;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getStartRegistration(): ?\DateTimeInterface
    {
        return $this->startRegistration;
    }

    public function setStartRegistration(\DateTimeInterface $startRegistration): static
    {
        $this->startRegistration = $startRegistration;

        return $this;
    }

    public function getEndRegistration(): ?\DateTimeInterface
    {
        return $this->endRegistration;
    }

    public function setEndRegistration(\DateTimeInterface $endRegistration): static
    {
        $this->endRegistration = $endRegistration;

        return $this;
    }

    public function getStartEvaluation(): ?\DateTimeInterface
    {
        return $this->startEvaluation;
    }

    public function setStartEvaluation(\DateTimeInterface $startEvaluation): static
    {
        $this->startEvaluation = $startEvaluation;

        return $this;
    }

    public function getEndEvaluation(): ?\DateTimeInterface
    {
        return $this->endEvaluation;
    }

    public function setEndEvaluation(\DateTimeInterface $endEvaluation): static
    {
        $this->endEvaluation = $endEvaluation;

        return $this;
    }
}
