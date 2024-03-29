$(document).ready(function() {
    displayPreview();
    var mediaName = $(".media-name");
    var mediaRow = $(".media-table-row");
    var left = $(".cnt-left").height();
    var right = $(".cnt-right").height();
    if (left > right){
        $(".cnt-right").css("height", left);
    }
    $(document).on("click",".recommend",function(){
        $("#recommend-box").show();
    });
    
    $.each(mediaRow, function(){
        var index = $(mediaRow).index(this);
        if ($(mediaName.eq(index)).text() == "."){
            $(mediaRow.eq(index)).hide();
        }
    });
});

function displayPreview() {
    var mediaName = $(".media-name");
    var mediaUrl = $(".media-url");
    var mediaRow = $(".media-table-row");
    var right = $("#wrap-right");
    $.each(mediaRow, function() {
        $(mediaName).click(function() {
            var index = $(mediaName).index(this);
            $(mediaRow).css("background-color", "#DCDFD4");
            $(mediaRow.eq(index)).css("background-color", "#B1D1FF");
            right.empty();
            var type = $(this).text().split(".")[1];
            if (type.match(/^doc$/i) || type.match(/^docx$/i) || type.match(/^txt$/i)) {
                right.append("Prévia não suportada.");
            }else if (type.match(/^pdf$/i)){
                right.append("<iframe height='433' width='552' src='" + mediaUrl.eq(index).text()  + "'/>");
            } else if (type.match(/^jpg$/i) || type.match(/^png$/i) || type.match(/^gif$/i)) {
                right.append("<img class='image-media' src='" + mediaUrl.eq(index).text() + "'/>");
            } else if (type.match(/^mp3$/i) || type.match(/^wav$/i) || type.match(/^wma$/i)) {
                right.append("<span class='audio-title'><p>" + $(this).text() + "</p></span>" + "<audio src='" + mediaUrl.eq(index).text() + "' controls='preload'/>");
            } else if (type.match(/^mp4$/i) || type.match(/^avi$/i) || type.match(/^mpeg$/i)) {
                right.append("<iframe height='310' width='552' src='" + mediaUrl.eq(index).text() + "' frameborder='0'/>");
            }
        });
    });
    mediaName.first().click();
}
function openRecommendBox() {
    $("#recommend-box").css("display", "block");
}
function closeRecommendBox() {
    $("#email-recommend").val("");
    $("#recommend-box").css("display", "none");
}