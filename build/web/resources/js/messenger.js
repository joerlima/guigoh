$(document).ready(function(){
    getCurriculumMessages();
    messengerFriends();
    setInterval(function() {
        messengerFriends()
    }, 60000);
    setInterval(function() {
        getMessages()
    }, 3000);
    $('#messenger_friends').on('click','li',openMessengerBox);
    $(document).on('click','.messageButton',openMessengerBox);
    $(document).on('click','#messageInputs #sendMessage',sendMessage);
    $(document).on('click', '#history', openAllHistory);
    $(".messenger").on('click','span',(function(){
        $("#messenger_friends").toggle();
    }));
    $(document).on('keypress','#messageInputs .textmessage',function(e){
        if(e.which == 13){
            $(this).parent().find('#sendMessage').click();
        }
    });
    
});


function getMessages(){
    var friend;
    $.ajax({ 
        url: "/webresources/primata/deliverMessages",
        dataType: "json", 
        data: {
            "socialProfileId":logged_social_profile_id
        },
        success: function(friends){
            for (var i=0;i<friends.length;i++){
                friend = friends[i];
                if($('#msg_'+friends[i].id).length==0){
                    if($('#msg_'+friends[i].id+' .flaghistory').length==0){
                        $.ajax({ 
                            url: "/webresources/primata/messagesHistory", 
                            dataType: "json", 
                            data: {
                                "loggedSocialProfileId":logged_social_profile_id,
                                "friendSocialProfileId":friends[i].id
                            },
                            success: function(history){
                                for (var i=0;i<history.length;i++){
                                    if($('#msg_'+friend.id).length==0){
                                        $('.messenger_boxes').append("<li id='msg_"+friend.id+"'><span>"+friend.name+"</span><span id='history'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_"+friend.id+"' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid="+friend.id+" id='sendMessage' type='button' value='enviar'></input></div></li>");
                                    }
                                    $('#msg_'+friend.id+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                                    $('#msg_'+friend.id+' #messages').scrollTop($('#msg_'+friend.id+' #messages').prop("scrollHeight"));
                                }
                            }
                            
                        });
                    }
                }
                var message = $("<p class='new_message'>"+friends[i].name +": "+friends[i].message+"</p>");
                $('#msg_'+friends[i].id).show();
                $('#msg_'+friends[i].id+' #messages').append(message);
                $('#msg_'+friends[i].id+' #messages').scrollTop($('#msg_'+friends[i].id+' #messages').prop("scrollHeight"));
            }
        }
    });
}

function getCurriculumMessages(){
    $.ajax({ 
        url: "/webresources/primata/getCurriculumMessages",
        dataType: "json", 
        data: {
            "socialProfileId":logged_social_profile_id
        },
        success: function(messages){
            for(var i=0;i<messages.length;i++){
                $('.curriculum_pop_ups').append("<li class='curriculum_pop_up'><p>"+messages[i].businessName+" está interessado no seu currículo!</p>"
                    + "<p>E-mail: "+messages[i].email+"</p><p>Telefone: "+messages[i].phone+"</p><p>"+messages[i].message+"</p>"
                    + "</li>")
                
                
            }
        }
    });
}

function messengerFriends(){
    $.ajax({
        type:"GET",
        url:"/webresources/primata/messengerFriends",
        data: {
            "socialProfileId":logged_social_profile_id
        },
        dataType: 'json',
        success:function(friends){
            var cont = 0;
            for(var i=0;i<friends.length;i++){
                if(friends[i].online == "true"){
                    cont = cont + 1;
                }
            }
            $('.messenger_container .messenger strong').text('('+cont+')'); 
            $('#messenger_friends p').html('');   
            $('#messenger_friends li').remove();    
            for (var i=0;i<friends.length;i++)
            {
                $('#messenger_friends').append("<li id='friend_"+friends[i].id+"' name='"+friends[i].name+"' socialprofileid='"+friends[i].id+"'>"+friends[i].name+"</li>");
                if(friends[i].online == "true"){
                    $('#friend_'+friends[i].id).css("color","green");
                }else{
                    $('#friend_'+friends[i].id).css("color","red");  
                }
            }
        }
    });
}

function openMessengerBox(){
    var name = $(this).attr('name');
    var socialProfileId = $(this).attr('socialprofileid');
    var createbox = "<li id='msg_"+socialProfileId+"'><span class='messenger_title'>"+name+"</span><span id='history' socialprofileid='"+socialProfileId+"'></span><div id='messages'></div><div id='messageInputs'><input id='textmessage_"+socialProfileId+"' class='textmessage' type='text' value=''></input><input type='hidden' class='flaghistory' value='1'/><input socialprofileid="+socialProfileId+" id='sendMessage' type='button' value='enviar'></input></div></li>";
    if($('#msg_'+socialProfileId).length>0){
        if( $('#msg_'+socialProfileId).css('visibility') == "hidden"){
            $('#msg_'+socialProfileId).css('visibility','visible');
            $('#textmessage_'+socialProfileId).focus().select();
        }
        else{
            $('#msg_'+socialProfileId).remove();
        }
    }else{
        var messenger_boxes_count = $('.messenger_boxes li').size();
        if(messenger_boxes_count != 0){
            var messenger_boxes_width = $('.messenger_boxes li').css('width').replace('px','');
            var body_size = $('body').css('width').replace('px','');
        }
        if(messenger_boxes_count == 0 || body_size * 1/2 > messenger_boxes_width * (messenger_boxes_count + 1)){
                
            $.ajax({ 
                url: "/webresources/primata/messagesHistory", 
                dataType: "json", 
                data: {
                    "loggedSocialProfileId":logged_social_profile_id,
                    "friendSocialProfileId":socialProfileId
                },
                error: function(history){
                    if($('#msg_'+socialProfileId).length==0){
                        $('.messenger_boxes').append(createbox);
                    }
                },
                success:function(history){
                    if($('#msg_'+socialProfileId).length==0){
                        $('.messenger_boxes').append(createbox);
                    }
                
                    for (var i=0;i<history.length;i++){
                        $('#msg_'+socialProfileId+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                        $('#msg_'+socialProfileId+' #messages').scrollTop($('#msg_'+socialProfileId+' #messages').prop("scrollHeight"));
                        $('#textmessage_'+socialProfileId).focus().select();
                    }
                }
            });
        }
    }
}

function openAllHistory(event){
    event.preventDefault();
    var socialProfileId = $(this).attr('socialprofileid');
    $('#msg_'+socialProfileId+' #messages p').remove();
    $.ajax({ 
        url: "/webresources/primata/allMessagesHistory", 
        dataType: "json", 
        data: {
            "loggedSocialProfileId":logged_social_profile_id,
            "friendSocialProfileId":socialProfileId
        },
        success:function(history){
                
            for (var i=0;i<history.length;i++){
                $('#msg_'+socialProfileId+' #messages').append("<p>"+history[i].name +": "+history[i].message+"</p>");
                $('#msg_'+socialProfileId+' #messages').scrollTop($('#msg_'+socialProfileId+' #messages').prop("scrollHeight"));
                $('#textmessage_'+socialProfileId).focus().select();
            }
        }
    });
}

function sendMessage(event){
    event.preventDefault();
    var id = $(this).attr('socialprofileid'); 
    var message = $('#textmessage_'+id).val();
    if(message != ""){
        $.ajax({
            type:"GET",
            url:"/webresources/primata/sendMessage",
            data: {
                "socialProfileIdSender":logged_social_profile_id,
                "socialProfileIdReceiver":id,
                "message": message
            },
            dataType: 'json',
            success:function(success){
                $('#msg_'+id+' #messages').append("<p> Você: "+message+"</p>");
                $('#msg_'+id+' #messages').scrollTop($('#msg_'+id+' #messages').prop("scrollHeight"));
                $('#msg_'+id+' #textmessage_'+id).val("");
            }
        });
    }
}