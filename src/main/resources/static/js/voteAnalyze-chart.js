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
    .attr("class", "arc");
arcs
    .append("path")
    .attr("fill", (d, i) => color_gender(i))
    .attr("d", arc);




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
    .attr("class", "arc");
arcs_job
    .append("path")
    .attr("fill", (d, i) => color_job(i))
    .attr("d", arc_job);
