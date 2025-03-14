<?php

namespace App\Entity;

use App\Repository\BakeryRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: BakeryRepository::class)]
class Bakery
{
    #[ORM\Id]
    #[ORM\Column(length: 17)]
    #[Groups(['Bakery:Read'])]
    #[ORM\GeneratedValue(strategy: 'NONE')]
    private ?string $siret = null;

    #[ORM\Column(length: 255)]
    #[Groups(['Bakery:Read'])]
    private ?string $name = null;

    #[ORM\Column(length: 255)]
    #[Groups(['Bakery:Read'])]
    private ?string $street = null;

    #[ORM\Column(length: 5)]
    #[Groups(['Bakery:Read'])]
    private ?string $postal_code = null;

    #[ORM\Column(length: 255)]
    #[Groups(['Bakery:Read'])]
    private ?string $city = null;

    #[ORM\Column(length: 14)]
    #[Groups(['Bakery:Read'])]
    private ?string $telephone_number = null;

    #[ORM\Column(length: 255)]
    private ?string $contact_name = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['Bakery:Read'])]
    private ?string $bakery_description = null;

    #[ORM\Column(type: Types::TEXT)]
    #[Groups(['Bakery:Read'])]
    private ?string $products_decription = null;

    #[ORM\Column]
    private ?\DateTimeImmutable $consent_date = null;

    #[ORM\OneToOne(targetEntity:User::class)]
    #[ORM\JoinColumn(name: 'user_id', referencedColumnName: 'id' , nullable: false, unique: true)]
    private ?User $user = null;

    /**
     * @var Collection<int, Evaluation>
     */
    #[ORM\OneToMany(targetEntity: Evaluation::class, mappedBy: 'bakery', orphanRemoval: true)]
    private Collection $evaluations;

    public function __construct()
    {
        $this->evaluations = new ArrayCollection();
    }

    public function getSiret(): ?string
    {
        return $this->siret;
    }

    public function setSiret(string $siret): static
    {
        $this->siret = $siret;

        return $this;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getStreet(): ?string
    {
        return $this->street;
    }

    public function setStreet(string $street): static
    {
        $this->street = $street;

        return $this;
    }

    public function getPostalCode(): ?string
    {
        return $this->postal_code;
    }

    public function setPostalCode(string $postal_code): static
    {
        $this->postal_code = $postal_code;

        return $this;
    }

    public function getCity(): ?string
    {
        return $this->city;
    }

    public function setCity(string $city): static
    {
        $this->city = $city;

        return $this;
    }

    public function getTelephoneNumber(): ?string
    {
        return $this->telephone_number;
    }

    public function setTelephoneNumber(string $telephone_number): static
    {
        $this->telephone_number = $telephone_number;

        return $this;
    }

    public function getContactName(): ?string
    {
        return $this->contact_name;
    }

    public function setContactName(string $contact_name): static
    {
        $this->contact_name = $contact_name;

        return $this;
    }

    public function getBakeryDescription(): ?string
    {
        return $this->bakery_description;
    }

    public function setBakeryDescription(string $bakery_description): static
    {
        $this->bakery_description = $bakery_description;

        return $this;
    }

    public function getProductsDecription(): ?string
    {
        return $this->products_decription;
    }

    public function setProductsDecription(string $products_decription): static
    {
        $this->products_decription = $products_decription;

        return $this;
    }

    public function getConsentDate(): ?\DateTimeImmutable
    {
        return $this->consent_date;
    }

    public function setConsentDate(\DateTimeImmutable $consent_date): static
    {
        $this->consent_date = $consent_date;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(User $user): static
    {
        $this->user = $user;

        return $this;
    }

    /**
     * @return Collection<int, Evaluation>
     */
    public function getEvaluations(): Collection
    {
        return $this->evaluations;
    }

    public function addEvaluation(Evaluation $evaluation): static
    {
        if (!$this->evaluations->contains($evaluation)) {
            $this->evaluations->add($evaluation);
            $evaluation->setBakery($this);
        }

        return $this;
    }

    public function removeEvaluation(Evaluation $evaluation): static
    {
        if ($this->evaluations->removeElement($evaluation)) {
            // set the owning side to null (unless already changed)
            if ($evaluation->getBakery() === $this) {
                $evaluation->setBakery(null);
            }
        }

        return $this;
    }
}
