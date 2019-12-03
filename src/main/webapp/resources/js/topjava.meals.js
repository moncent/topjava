var ajaxUrl = "ajax/meals/";

$(function() {
   makeEditable({
       ajaxUrl : ajaxUrl,
       datatableApi:$("#datatables").DataTable({
           "paging": false,
           "info": false,
           "searching":false,
           "columns": [
               {
                   "data": "dateTime"
               },
               {
                   "data": "description"
               },
               {
                   "data": "calories"
               },
               {
                   "defaultContent": "Edit",
                   "orderable": false
               },
               {
                   "defaultContent": "Delete",
                   "orderable": false
               }
           ],
           "order": [
               [
                   0,
                   "desc"
               ]
           ]
       })
   });

   $("#dateTime").datetimepicker({
        uiLibrary: 'bootstrap4',
        modal: true,
        footer: true
    }).show();

   $("#startDate").datetimepicker({
        uiLibrary: 'bootstrap4',
        modal: true,
        footer: true,
        timepicker: false,
        format:'Y-m-d'
   }).show();

   $("#endDate").datetimepicker({
        uiLibrary: 'bootstrap4',
        modal: true,
        footer: true,
        timepicker: false,
        format:'Y-m-d'
   }).show();

   $("#startTime").datetimepicker({
        uiLibrary: 'bootstrap4',
        modal: true,
        footer: true,
        datepicker: false,
        format: 'H:m'
   }).show();

   $("#endTime").datetimepicker({
        uiLibrary: 'bootstrap4',
        modal: true,
        footer: true,
        datepicker: false,
        format: 'H:m'
   }).show();

   $('#filterForm').submit(function(e) {
        e.preventDefault();
        ajaxUrl += "filter";
        $.ajax({
            method: "GET",
            url: ajaxUrl,
            contentType: "application/json",
            dataType:'json',
            data: {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val(),
                startTime: $('#startTime').val(),
                endTime: $('#endTime').val()
            },
            cache: true,
            success: function (data) {
                context.datatableApi.clear().rows.add(data).draw();
            }
        });
   });
});