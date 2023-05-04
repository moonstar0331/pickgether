$(document).ready(function () {
    bar_graph_age();
});

function bar_graph_age() {
    var maxIndex = $(".graph-bar").length;

    for (var i = 0; i < maxIndex; i++) {
        var val = !!$(".graph-bar").eq(i).attr('value') ? $(".graph-bar").eq(i).attr('value') : 0;
        var color = $(".graph-bar").eq(i).attr('graph-color');
        $(".graph-bar").eq(i).css({
            "background": color
        }).animate({
            "height": val + "%"
        }, 800);
        $(".graph-bar-label").eq(i).text((i + 1) * 10 + "대").css("font-weight", "bold");
        $(".graph-bar-tag").eq(i).text(val + "%").css("font-weight", "bold");
        $(".graph-bar-tag").eq(i).css("position", "absolute");
        $(".graph-bar-tag").eq(i).css("bottom", val);
        $(".graph-bar-tag").eq(i).css("left", "1rem");
    }
}

const svgDimension = {
    width: 300,
    height: 300
};
const radius = Math.min(svgDimension.width, svgDimension.height) / 2;


/* pie graph (gender) */
const data_gender = [$("#gender-M").attr('value'), $("#gender-F").attr('value')];

const svg = d3
    .select("#pie-chart-gender")
    .append("svg")
    .attr("width", svgDimension.width)
    .attr("height", svgDimension.height)
    .style("display", "block")
    .style("margin", "auto");

const g = svg
    .append("g")
    .attr("transform", `translate(${svgDimension.width / 2}, ${svgDimension.height / 2})`);
const color_gender = d3.scaleOrdinal([
    "#F4CEC3", "#ECE2B1", "#DDECB1"
]);

const pie = d3.pie();
const arc = d3.arc().innerRadius(0).outerRadius(radius);
const arcs = g
    .selectAll("arc")
    .data(pie(data_gender))
    .enter()
    .append("g")
    .attr("class", "arc")
    .on("mouseover", onMouseOver_gender)
    .on("mouseout", onMouseOut_gender);
arcs
    .append("path")
    .attr("fill", (d, i) => color_gender(i))
    .attr("d", arc);
arcs
    .append("text")
    .attr("transform", (d) => `translate(${arc.centroid(d)})`)
    .text((d) => d.value + "%")
    .attr("font-family", "sans-serif")
    .attr("font-size", "18px")
    .attr("font-weight", "bold")
    .attr("fill", "#fff")
    .attr("text-anchor", "middle")
    .attr("display", "none");


/* pie graph (job) */
const data_job = [!!$("#job-expert").attr('value') ? $("#job-expert").attr('value') : 0,
    !!$("#job-office").attr('value') ? $("#job-office").attr('value') : 0,
    !!$("#job-service").attr('value') ? $("#job-service").attr('value') : 0,
    !!$("#job-sail").attr('value') ? $("#job-sail").attr('value') : 0];

const svg_job = d3
    .select("#pie-chart-job")
    .append("svg")
    .attr("width", svgDimension.width)
    .attr("height", svgDimension.height)
    .style("display", "block")
    .style("margin", "auto");

const g_job = svg_job
    .append("g")
    .attr("transform", `translate(${svgDimension.width / 2}, ${svgDimension.height / 2})`);
const color_job = d3.scaleOrdinal([
    "#F4CEC3", "#ECE2B1", "#DDECB1", "#CEF3E1"
]);

const pie_job = d3.pie();
const arc_job = d3.arc().innerRadius(0).outerRadius(radius);
const arcs_job = g_job
    .selectAll("arc")
    .data(pie_job(data_job))
    .enter()
    .append("g")
    .attr("class", "arc")
    .on("mouseover", onMouseOver_job)
    .on("mouseout", onMouseOut_job);
arcs_job
    .append("path")
    .attr("fill", (d, i) => color_job(i))
    .attr("d", arc_job);
arcs_job
    .append("text")
    .attr("transform", (d) => `translate(${arc.centroid(d)})`)
    .text((d) => d.value + "%")
    .attr("font-family", "sans-serif")
    .attr("font-size", "18px")
    .attr("font-weight", "bold")
    .attr("fill", "#fff")
    .attr("text-anchor", "middle")
    .attr("display", "none");


function onMouseOut_gender(d, i) {
    d3.select(this)
        .select("path")
        .transition()
        .duration(200)
        .style("fill", color_gender(i));
    d3.select(this).select("text").attr("display", "none");
}

function onMouseOver_gender(d, i) {
    d3.select(this)
        .select("path")
        .transition()
        .duration(200)
        .style("fill", "#BCB5D2");
    d3.select(this).select("text").attr("display", "block");
}

function onMouseOut_job(d, i) {
    d3.select(this)
        .select("path")
        .transition()
        .duration(200)
        .style("fill", color_job(i));
    d3.select(this).select("text").attr("display", "none");
}

function onMouseOver_job(d, i) {
    d3.select(this)
        .select("path")
        .transition()
        .duration(200)
        .style("fill", "#BCB5D2");
    d3.select(this).select("text").attr("display", "block");
}