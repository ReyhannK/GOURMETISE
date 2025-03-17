<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250317104342 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE refresh_tokens (id INT AUTO_INCREMENT NOT NULL, refresh_token VARCHAR(128) NOT NULL, username VARCHAR(255) NOT NULL, valid DATETIME NOT NULL, UNIQUE INDEX UNIQ_9BACE7E1C74F2195 (refresh_token), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE evaluation ADD CONSTRAINT FK_1323A5759B834B34 FOREIGN KEY (bakery_siret) REFERENCES bakery (siret)');
        $this->addSql('ALTER TABLE note CHANGE evaluation_id evaluation_id INT NOT NULL, CHANGE criteria_id criteria_id INT NOT NULL');
        $this->addSql('ALTER TABLE note ADD CONSTRAINT FK_CFBDFA14456C5646 FOREIGN KEY (evaluation_id) REFERENCES evaluation (id)');
        $this->addSql('ALTER TABLE note ADD CONSTRAINT FK_CFBDFA14990BEA15 FOREIGN KEY (criteria_id) REFERENCES criteria (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE refresh_tokens');
        $this->addSql('ALTER TABLE evaluation DROP FOREIGN KEY FK_1323A5759B834B34');
        $this->addSql('ALTER TABLE note DROP FOREIGN KEY FK_CFBDFA14456C5646');
        $this->addSql('ALTER TABLE note DROP FOREIGN KEY FK_CFBDFA14990BEA15');
        $this->addSql('ALTER TABLE note CHANGE evaluation_id evaluation_id BIGINT NOT NULL, CHANGE criteria_id criteria_id BIGINT NOT NULL');
    }
}
