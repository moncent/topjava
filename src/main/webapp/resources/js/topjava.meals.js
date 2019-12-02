$(function() {
   makeEditable({
       ajaxUrl:"ajax/meals/",
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
       footer: true,
   }).show();
});


