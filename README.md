##Задания проекта


##Общее описание задачи
Организация мероприятий. Сервис, который полезен и организаторам,
т.к. в нем можно создавать задачи, которые можно распределить между 
организаторской командой, и участникам, т.к. можно зарегистрироваться
на какое-либо мероприятие.    
   
###Моя задача: 
#####спринт 1
Написать микросервис review_service. Эндпоинты:

* POST /reviews - создать отзыв

* PATCH /reviews/{id} - обновить отзыв (обновить может только автор (проверяем по header), нельзя обновить authorId, createdDateTime, updatedDateTime, eventId)

* GET /reviews/{id} - получить отзыв по id (вернуть без authorId)

* GET /reviews?page={page}&size={size}&eventId={eventId} - получить отзывы с пагинацией и обязательно по заданному id события (вернуть без authorId)

* DELETE /reviews/{id} - удалить отзыв (проверка по header, что удаляет автор)

Модель Review включает следующие поля: authorId, username, title, content, createdDateTime, updatedDateTime, mark, eventId