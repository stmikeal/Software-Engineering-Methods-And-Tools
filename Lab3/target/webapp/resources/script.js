function onlyDigits() {
  const separator = this.dataset.separator;
  const replaced = new RegExp('[^\\d\\' + separator + '\\-]', "g");
  const regex = new RegExp('\\' + separator, "g");
  this.value = this.value.replace(replaced, "");

  const minValue = parseFloat(this.dataset.min);
  const maxValue = parseFloat(this.dataset.max);
  const val = parseFloat(separator === "." ? this.value : this.value.replace(
      new RegExp(separator, "g"), "."));
  if (minValue <= maxValue) {
    if (this.value[0] === "-") {
      if (this.value.length > 8) {
        this.value = this.value.substr(0, 8);
      }
    } else {
      if (this.value.length > 7) {
        this.value = this.value.substr(0, 7);
      }
    }

    if (minValue < 0 && maxValue < 0) {
      if (this.value[0] !== "-") {
        this.value = "-" + this.value[0];
      }
    } else if (minValue >= 0 && maxValue >= 0) {
      if (this.value[0] === "-") {
        this.value = this.value.substr(0, 0);
      }
    }

    if (val < minValue || val > maxValue) {
      this.value = this.value.substr(0, 0);
    }

    if (this.value.match(regex)) {
      if (this.value.match(regex).length > 1) {
        this.value = this.value.substr(0, 0);
      }
    }

    if (this.value.match(/\-/g)) {
      if (this.value.match(/\-/g).length > 1) {
        this.value = this.value.substr(0, 0);
      }
    }
  }
}

function reset() {
  let canvas = document.querySelector("#canvas");
  const context = canvas.getContext('2d');
  context.clearRect(0, 0, canvas.width, canvas.height);

  drawGraph();
}

function onAnswer(res) {
  console.log("принято");
  let json = JSON.stringify(res);
  createRow(json);

}

function loadTable() {
  if (localStorage.getItem("points") !== null) {
    createRow(localStorage.getItem("points"));
  }
}

function createRow(row) {
  let data = JSON.parse(row);

  historyTable.innerHTML = "";

  for (let i in data) {
    drawPoint(data[i]);

    let x = parseFloat(data[i].x.replace(new RegExp(",", "g"), ".")).toFixed(2);
    let y = parseFloat(data[i].y.replace(new RegExp(",", "g"), ".")).toFixed(2);

    if (($('#historyTable').is(':empty'))) {
      historyTable.innerHTML = "<tr><th>Точка</th>" +
          "<th>Параметр R</th>" +
          "<th>Отправка</th>" +
          "<th>Исполнение</th>" +
          "<th>Результат</th></tr>"
    }

    let result;
    result = "<tr class='historyTd'>";
    result += `<td class='historyElem'> (${x}, ${y}) </td>`;
    result += `<td class='historyElem'> ${parseInt(data[i].r)} </td>`;
    result += `<td class='historyElem'> ${data[i].currentTime} </td>`;
    result += `<td class='historyElem'> ${(parseFloat(
        Math.random() / 10).toFixed(3))} ms</td>`;
    if (data[i].hit === "true") {
      result += `<td class='historyElem'> Попадание </td>`;
    } else {
      result += `<td class='historyElem'> Промах </td>`;
    }

    result += "</tr>";

    historyTable.innerHTML = historyTable.innerHTML + result;

  }
  localStorage.setItem("points", JSON.stringify(data));

}

function drawPoint(point) {
  const canvas = document.getElementById('canvas');
  const ctx = canvas.getContext('2d');

  const r = document.getElementById("field-form:inputR").innerHTML;

  ctx.beginPath();
  ctx.arc(
      parseFloat(point.x) * 118.8 /
      parseInt(r) + 150,
      (-1) * parseFloat(point.y) * 118.8
      / parseInt(r) + 150, 2, 0,
      2 * Math.PI, false);
  if (point.hit === "Попадание") {
    ctx.fillStyle = '#31c73e';
    ctx.strokeStyle = '#99da90';
  } else {
    ctx.fillStyle = '#9A9898';
    ctx.strokeStyle = '#6b6b6b';
  }
  ctx.fill();
  ctx.lineWidth = 0.4;
  ctx.stroke();
}

function checkFields() {
  let x = document.getElementById("field-form:inputX").value;
  let y = document.getElementById("field-form:inputY").value;
  let r = document.getElementById("field-form:inputR").innerHTML;

  if (isNaN(x) || y === "") {
    alert("Заполните все поля");
  } else {
    x = parseFloat(x.toString().replace(new RegExp(",", "g"), "."));
    y = parseFloat(y.toString().replace(new RegExp(",", "g"), "."));
    send(x, y, r);
  }
}

function send(x, y, r) {
  $.ajax({
    type: "POST",
    url: "controller",
    data: {
      "x": x,
      "y": y,
      "r": r,
      "time": (new Date()).getTimezoneOffset()
    },
    success: onAnswer,
    dataType: "json"
  });
  console.log("отправлено");
}

function drawGraph() {
  let canvas = document.querySelector("#canvas");
  let ctx = canvas.getContext("2d");

  const img = new Image();
  img.src = "resources/img/graph.svg"
  img.width = 300;
  img.height = 300;
  img.addEventListener("load", function() {
    ctx.mozImageSmoothingEnabled = false;
    ctx.webkitImageSmoothingEnabled = false;
    ctx.msImageSmoothingEnabled = false;
    ctx.imageSmoothingEnabled = false;
    ctx.drawImage(img, 0, 0, 300, 300);
  }, false);

  // img.onload = function () {
  //   ctx.mozImageSmoothingEnabled = false;
  //   ctx.webkitImageSmoothingEnabled = false;
  //   ctx.msImageSmoothingEnabled = false;
  //   ctx.imageSmoothingEnabled = false;
  //   ctx.drawImage(img, 0, 0, 300, 300);
  // };

}

function addMouseEvent(){
  let canvas = document.querySelector("#canvas");

  canvas.addEventListener('mousedown', function (e) {
    const rect = canvas.getBoundingClientRect()

    const x = e.clientX - rect.left - 43,
        y = e.clientY - rect.top - 43;

    const r = document.getElementById("field-form:inputR").innerHTML;
    let oldX = document.getElementById("field-form:inputX").value;
    let oldY = document.getElementById("field-form:inputY").value;

    document.getElementById("field-form:inputX").value = ((x - 150) * r) / 118.8;
    document.getElementById("field-form:inputY").value = (y - 150) * r / (-118.8);

    document.getElementById("field-form:submit-button").click();

    document.getElementById("field-form:inputX").value = oldX;
    document.getElementById("field-form:inputY").value = oldY;

    document.getElementById("field-form:invisible-button").click();
  }, false)
}

function loadPointsFromTable(){
  let $table_rows = document.querySelector("#historyTable").tBodies[0].rows;
  if ($table_rows[0].cells[0].innerText === '') return;
  let points = [];
  for (let i = 0; i < $table_rows.length; ++i){
    let point = {};
    point.x = parseFloat($table_rows[i].cells[0].innerText.replace(",","."));
    point.y = parseFloat($table_rows[i].cells[1].innerText.replace(",","."));
    point.hit = $table_rows[i].cells[5].innerText.trim();
    points.push(point);
    console.log(i);
  }
  points.forEach(point => { drawPoint(point); });
}

// document.getElementById("field-form:inputY").oninput = onlyDigits;
// document.querySelector("#submit-button").onclick = checkFields;
document.getElementById("field-form:reset-button").onclick = reset;

// document.querySelector("#rSelect").addEventListener("change", redrawPoints);

drawGraph();
loadPointsFromTable();
// loadTable();
addMouseEvent();

