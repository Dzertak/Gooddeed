package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatRepositoryModel {

    //он хранит в себе idUser, imageInitiative и Title
    private Initiative initiative;
    //такое же значение как и у initiativeId. Так что можно будет обойтись без этой переменной
    private String currentChatRoomId;
    //в этом списке мы могли хранить текст сообщения, userId отправителя, Имя отправителя, время отправления. Если не ошибаюсь то это всё
    private List<MessageEntity> messages;

    //При открытии комнаты-чата, нам нужно будет в репозитории заполнить данными этот класс-модель и передать на фрагмент
}
