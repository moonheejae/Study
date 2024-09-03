"use strict"; // Strict 모드를 활성화

// 번역
const CommonTranslation = {
    commonTranslation: function(formData, successCallback) {
        $.ajax({
            url : deFaultUrl + "/translation",
            type: 'POST',
            data: JSON.stringify(formData),
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            async: false,
            success: successCallback,
            error: function (request, status, error) {
                console.log("code : " + request.status + " / error : " + error);
            }
        });
    },
    ContentTranslation: function ( content, isCommentTranslated ) {
        if( isContentTranslated === true ){
            content.text( content.attr('data-value') );
            return false;
        } else {
            const formData = {
                content : content.text(),
                user_country : UserCountry
            }
            this.contentTranslation( formData,
                function (response){
                    content.text(response.data.content);
                });
            return true;
        }
    }
};