var mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

function saveWithDateTimeConverter() {
    var selectorDt = $("#dateTime");
    var dt = moment(selectorDt.val()).format('Y-MM-DD[T]HH:mm:ss');
    selectorDt.val(dt);
    save();
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            var dt = moment(data).format('Y-MM-DD HH:mm');
                            return dt;
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function(row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
    $('#dateTime').datetimepicker({
        format: 'Y\-m\-d H:i'
    });

    $('#startDate').datetimepicker({
        timepicker: false,
        format: 'Y\-m\-d'
    });

    $('#endDate').datetimepicker({
        timepicker: false,
        format: 'Y\-m\-d'
    });

    $('#startTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });

    $('#endTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
});