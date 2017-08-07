</div>
</div>

<!-- Scripts -->

<script src="<?= asset('js/app.js') ?>"></script>
<script src="<?= asset('js/plugins.js') ?>"></script>
<script>
$(function() {
  $('.datetime-picker').datetimepicker({
    format        : 'dd/mm/yyyy hh:ii',
    autoclose     : true,
    todayHighlight: true,
    useCurrent    : true,
    minDate       :moment(),
  });

  $('.date-picker').datetimepicker({
    format        : 'dd/mm/yyyy',
    autoclose     : true,
    todayHighlight: true,
    defaultDate   : moment(),
    useCurrent    : true,
  });
});

$(".select2").select2({
  allowClear: true
});

$(".switch").bootstrapSwitch();

$('[data-toggle="popover"]').popover();

function deleteItem(slug, idAttr, nameAttr, message, deleteClass = "delete") {
  $('.' + deleteClass).on('click', function (e) {
    var id = $(this).attr(idAttr);
    var name = $(this).attr(nameAttr);

    swal({
      title: "Are you sure?",
      text:  "'" + name + "' " + message,
      type: "warning",
      confirmButtonColor: "#DD6B55",
      confirmButtonText: "Yes, delete!",
      showCancelButton: true,
      cancelButtonText: "No",
      closeOnConfirm: false
    }).then(function () {
      $.ajax({
        url: "/" + slug + "/delete/" + id,
        method: "DELETE",
        success: function(result){
          $("#" + slug + "-" + id).remove();
          swal({
            title: "Successfully Deleted!",
            type: "success",
            confirmButtonText: "Ok",
          });
        },
        error: function (xhr, ajaxOptions, thrownError) {
          ajaxError(xhr, ajaxOptions, thrownError);
        }
      });
    })
  });
}

function block(selector) {
  $(selector).block({
    // message: '<img style="width:100%; height:auto;" src="{{ asset('front/images/gifs/loading.gif') }}" />',
    overlayCSS: { backgroundColor: 'rgb(255, 255, 255)' },
    css: { border: 'none' },
  });
}

function unblock(selector) {
  $(selector).unblock();
}


function ajaxError(xhr, ajaxOptions, thrownError) {
  console.log("XHR:");
  console.log(xhr);
  console.log("Ajax Options:");
  console.log(ajaxOptions);
  console.log("Thrown Error:");
  console.log(thrownError);
  swal({
    title: "Bir hata ile karşılaşıldı!",
    type: "error",
    confirmButtonText: "Tamam",
  });
}


</script>
