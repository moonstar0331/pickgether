const svgDimension = {
    width: 300,
    height: 300
};
const radius = Math.min(svgDimension.width, svgDimension.height) / 2;


/* pie graph (gender) */
const data_gender = [10, 20, 70];

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
    .text((d) => d.value +"%")
    .attr("font-family", "sans-serif")
    .attr("font-size", "18px")
    .attr("font-weight", "bold")
    .attr("fill", "#fff")
    .attr("text-anchor", "middle")
    .attr("display", "none");



/* pie graph (job) */
const data_job = [10, 20, 50, 30];

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
    .text((d) => d.value +"%")
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