require: slotfilling/slotFilling.sc
  module = sys.zb-common
 
theme: /ChangePinCode
    
    state: Start
        q!: $regex</start>
        script:
            $jsapi.startSession()

    state: CardOrApp
        q: *(пин*/код*/парол*)*
        a: Здраствуйте!
        a: Сейчас расскажу порядок действий. Выберите, что именно планируете сделать:\n1. Поменять пароль для входа в приложение.\n2. Поменять PIN-код от карты.\nПожалуйста, отправьте цифру, соответствующую вашему выбору.   
        script:
            $reactions.timeout({interval: "20 seconds", targetState: "/ChangePinCode/End"})
        
        state: ChoseOne
            q:*(1*/приложен*)*
            go!:/ChangePinCode/One
        
        state: ChoseTwo
            q:*(2*/карт*)*
            go!:/ChangePinCode/Two

    state: One
        q: *(*(мен*/забыл*/восстан*)* *(пин*/код*/пароль*)* *приложен*)*
        a: Смена пароля от приложения возможна несколькими способами:\n1. на экране "Профиль" выберите "Изменить код входа в приложение ".\n2. введите SMS-код.\n3. придумайте новый код для входа в приложение и повторите его.
        script:
            $reactions.timeout({interval: "2 seconds", targetState: "/ChangePinCode/One/One_addition"})
        
        state: One_addition
            a: Либо нажмите на кнопку "Выйти" на странице ввода пароля для входа в приложение.\n\nПосле чего нужно будет заново пройти регистрацию:
                \n1. ввести полный номер карты (если оформляли ранее, иначе номер телефона и дату рождения),
                \n2. указать код из смс-код,
                \n3. придумать новый пароль для входа.
            script:
                $reactions.timeout({interval: "2 seconds", targetState: "/ChangePinCode/One/One_addition/One_end"})
            
            state: One_end
                a: Приятно было пообщаться. Всегда готов помочь вам снова 😉
                go!:/ChangePinCode/End
                
    state: Two
        q: *(*(мен*/забыл*/восстан*)* *(пин*/код*/пароль*)* *карт*)*
        a: Это можно сделать в приложении:
            \n1. На экране "Мои деньги " в разделе "Карты" нажмите на нужную.
            \n2. Выберите вкладку "Настройки".
            \n3. Нажмите "Сменить пин-код".
            \n4. И введите комбинацию, удобную вам.
            \n5. Повторите её.
        a: И все готово!\nПин-код установлен, можно пользоваться. 😁
        a: Приятно было пообщаться. Всегда готов помочь вам снова 😉
        go!:/ChangePinCode/End
        
    state: Bye
        q!: *(пок*/свидан*/спас*)*
        a: Спасибо за ваше обращение!
        go!:/ChangePinCode/End

    state: End
        script:
            $jsapi.stopSession()
    
    state: NoMatch
        event!: noMatch 
        random:
            a: Извините, я не понимаю. Переформулируйте, пожалуйста.
            a: Простите, кажется, я не понял. Задайте свой вопрос иначе. 