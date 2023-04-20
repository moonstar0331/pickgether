/* 세로 막대 그래프 */
$(document).ready(function(){
    bar_graph_age();
    pie_graph_gender();
    pie_graph_job();
});

/**
 * 연령대 및 성별
 */

/* 세로 막대 그래프 */
function bar_graph_age() {
    var maxIndex = $(".graph-bar").length;

    for(var i=0; i<maxIndex; i++){
        var val = $(".graph-bar").eq(i).attr('graph-val');
        var color = $(".graph-bar").eq(i).attr('graph-color');
        $(".graph-bar").eq(i).css({
            "left": (i+1)*80+"px",
            "background":color
        }).animate({
            "height":val+"%"
        },800);
    }
}

/* 파이 그래프 -  */
function pie_graph_gender() {
    var i=1;
    var func1 = setInterval(function(){
        if(i<26){
            color1_gender(i);
            i++;
        } else if(i<70){
            color2_gender(i);
            i++;
        } else if(i<101){
            color3_gender(i);
            i++;
        } else {
            clearInterval(func1);
        }
    },10);
}

function color1_gender(i){
    $(".pie-chart-gender").css({
        "background":"conic-gradient(#8b22ff 0% "+i+"%, #ffffff "+i+"% 100%)"
    });
}
function color2_gender(i){
    $(".pie-chart-gender").css({
        "background":"conic-gradient(#8b22ff 0% 25%, #ffc33b 25% "+i+"%, #ffffff "+i+"% 100%)"
    });
}
function color3_gender(i){
    $(".pie-chart-gender").css({
        "background":"conic-gradient(#8b22ff 0% 25%, #ffc33b 25% 70%, #21f3d6 70% "+i+"%, #ffffff "+i+"% 100%)"
    });
}

/**
 * 직업
 */

/* 파이 그래프 */
function pie_graph_job() {
    var i=1;
    var func1 = setInterval(function(){
        if(i<26){
            color1_job(i);
            i++;
        } else if(i<70){
            color2_job(i);
            i++;
        } else if(i<85){
            color3_job(i);
            i++;
        } else {
            clearInterval(func1);
        }
    },10);
}

function color1_job(i){
    $(".pie-chart-job").css({
        "background":"conic-gradient(#F4CEC3 0% "+i+"%, #ffffff "+i+"% 100%)"
    });
}
function color2_job(i){
    $(".pie-chart-job").css({
        "background":"conic-gradient(#F4CEC3 0% 25%, #ECE2B1 25% "+i+"%, #ffffff "+i+"% 100%)"
    });
}
function color3_job(i){
    $(".pie-chart-job").css({
        "background":"conic-gradient(#F4CEC3 0% 25%, #ECE2B1 25% 70%, #DDECB1 70% "+i+"%, #ffffff "+i+"% 100%)"
    });
}