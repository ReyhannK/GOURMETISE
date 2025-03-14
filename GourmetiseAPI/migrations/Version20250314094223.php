<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250314094223 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE criteria (id INT AUTO_INCREMENT NOT NULL, label VARCHAR(255) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE evaluation (id INT AUTO_INCREMENT NOT NULL, bakery_siret VARCHAR(17) NOT NULL, code_ticket VARCHAR(255) NOT NULL, date_evaluation DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', INDEX IDX_1323A5759B834B34 (bakery_siret), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE note (evaluation_id INT NOT NULL, criteria_id INT NOT NULL, value INT NOT NULL, INDEX IDX_CFBDFA14456C5646 (evaluation_id), INDEX IDX_CFBDFA14990BEA15 (criteria_id), PRIMARY KEY(evaluation_id, criteria_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE evaluation ADD CONSTRAINT FK_1323A5759B834B34 FOREIGN KEY (bakery_siret) REFERENCES bakery (siret)');
        $this->addSql('ALTER TABLE note ADD CONSTRAINT FK_CFBDFA14456C5646 FOREIGN KEY (evaluation_id) REFERENCES evaluation (id)');
        $this->addSql('ALTER TABLE note ADD CONSTRAINT FK_CFBDFA14990BEA15 FOREIGN KEY (criteria_id) REFERENCES criteria (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE evaluation DROP FOREIGN KEY FK_1323A5759B834B34');
        $this->addSql('ALTER TABLE note DROP FOREIGN KEY FK_CFBDFA14456C5646');
        $this->addSql('ALTER TABLE note DROP FOREIGN KEY FK_CFBDFA14990BEA15');
        $this->addSql('DROP TABLE criteria');
        $this->addSql('DROP TABLE evaluation');
        $this->addSql('DROP TABLE note');
    }
}
