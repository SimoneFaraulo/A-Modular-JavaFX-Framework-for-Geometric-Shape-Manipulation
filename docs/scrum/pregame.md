# Pre-Game Scrum – Linee Guida di Progetto

## 1. Coding Convention

### Stile generale

* **Lingua codice e commit**: inglese
* Usa nomi **espliciti** e **consistenti**

### Java Coding Convention

* **Naming convention**:

  * Classi: `PascalCase`
  * Metodi e variabili: `camelCase`
  * Costanti: `MAIUSCOLO_CON_UNDERSCORE`

* **Formattazione del codice**:

  * Spaziatura uniforme
  * Indentazione corretta (usare macro IDE per il refactoring)
  * Uso delle parentesi graffe `{}` anche per blocchi monoriga

* **Definizione delle costanti**:

  * Evitare magic numbers, usare costanti simboliche in modo da
    rendere più semplice la modifica e la comprensione del codice

* **Commenti e documentazione**:

  * JavaDoc per tutte le classi e i metodi
  * Commenti chiari dove necessario per spiegare algoritmi non banali
  * Evitare commenti ovvi o ridondanti

* **Struttura e modularità**:

  * Metodi piccoli e riutilizzabili per operazioni complesse
  * Classi contenute e coerenti con il principio di responsabilità singola (SRP)

* **Testing**:

  * Test con nomi descrittivi secondo lo standard JUnit

* **Package naming**:

  * Solo lowercase, senza underscore, secondo le convenzioni Java. Esempio: `com.miodominio.nomeprogetto.modulo`

---

## 2. Definition of Done

* *JavaDoc completa e aggiornata*: classi, interfacce e metodi pubblici devono essere documentati in modo chiaro

* *Refactoring effettuato*: il codice deve essere stato migliorato in termini di leggibilità e manutenibilità, senza alterare il comportamento

* *Revisione di gruppo completata*: almeno un altro membro del team ha letto, compreso e approvato il codice. Eventuali dubbi o richieste di modifica sono stati risolti

* *Codice compilabile senza errori*: il progetto deve compilare correttamente con il sistema di build Maven

* *Unit test completi e superati*: tutti i test unitari devono essere scritti ed eseguiti con successo, sia individualmente che in fase di integrazione.

* *Test di integrazione superati*: i test di integrazione necessari devono essere presenti ed eseguiti con esito positivo

* *Dipendenze corrette*: tutte le librerie devono essere dichiarate correttamente nel progetto, senza conflitti

* *Stile di codice conforme*: il codice rispetta tutte le convenzioni di stile condivise dal gruppo

* *Commit e push corretti*: le modifiche sono state committate e pushate su Git, con messaggi chiari e coerenti

* *User stories tracciate su Trello*: le funzionalità completate devono essere segnate come *Done* sulla board di Trello
