# issue-tracker
Приложение issue-tracker предназначено для создания задач и их назначения. The application is based on [CUBA platform](https://www.cuba-platform.com/).

##Структура приложения

Приложение содержит две сущности:

* Project - Проект.
    * shortName - короткое имя проекта. Необходимо для формирования уникального номера задачи.
     Номер задачи формируется из *shortName* проекта и значения последовательности для данного проекта.
     Подробную реализацию смотреть в *com.company.tracker.listener.IssueListener*.
    * name - полное имя проекта.
    * category - сущность содержащая набор дополнительных атрибутов для задачи, которая создается на основе этого проекта.
      Тип атрибута сущность - [Category](https://doc.cuba-platform.com/manual-6.2-ru/manual.html#dynamic_attributes) из базового проекта CUBA.
    * team - набор пользователей, которым будет доступен этот проект. Пользователи смогут создавать и просматривать задачи на основе этого проекта.        
* Issue - Проблема(Задача в рамках проекта)
    * project - проект. В зависимости от того какой проект выбран, в редакторе задачи будут отображен список дополнительных(динамических) атрибутов.
    * number - номер задачи. Необходимо для формирования уникального номера задачи. См. 'Project#shortName'.
    * summary - короткое описание задачи
    * description - полное описание задачи
    * assignee - пользователь на которого назначена задача
    * created - пользователь создавший задачу.

##Пример

1. Запустить приложение
2. Открыть экран **Project/Project browser**
3. Создать новый проект *Short Name* - PL; *Name* - Platform;
4. Создать новую категорию
5. Добавить новый атрибут в категорию *Название* - Type; *Код* - PL_ISSUE_TYPE; *Тип атрибута* - Перечисление;
 *Перечисление* - Bug,Exception,Usability Problem,Cosmetics,Feature;
6. Сохранить
7. Создать новый проект *Short Name* - ST; *Name* - Studio;
8. Создать новую категорию
9. Добавить новый атрибут в категорию *Название* - Priority; *Код* - ST_ISSUE_PRIORITY; *Тип атрибута* - Перечисление;
    *Перечисление* - Major,Normal,Minor;
10. Сохранить
11. Открыть экран **Project/Issue browser**
12. Создать новую задачу. При изменение значения поля *Project* будет меняться набор динамических атрибутов.  