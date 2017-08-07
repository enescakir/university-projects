<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Administrators</h3>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="administrators-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($administrators as $administrator) : ?>
                    <tr id='administrator-<?= $administrator->id ?>' class="<?= $administrator->activated_at ? "success" : "danger"; ?>">
                      <td><?= $administrator->id ?></td>
                      <td><?= $administrator->name ?></td>
                      <td><?= $administrator->email ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <?php if (!$administrator->activated_at) : ?>
                            <button class="activate btn btn-success btn-xs" type="button"
                              administrator-id="<?= $administrator->id ?>"
                              administrator-name="<?= $administrator->name ?>"><i class="fa fa-check"></i>
                            </button>
                          <?php endif; ?>
                      </div>
                    </td>
                  </tr>
                <?php endforeach; ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</div>

<?php partial_view('scripts') ?>
<script type="text/javascript">
  $('.activate').on('click', function (e) {
    var id = $(this).attr('administrator-id');
    var name = $(this).attr('administrator-name');

    swal({
      title: "Are you sure?",
      text:  "You are activating '" + name + "'",
      type: "warning",
      confirmButtonColor: "#DD6B55",
      confirmButtonText: "Yes, activate!",
      showCancelButton: true,
      cancelButtonText: "No",
      closeOnConfirm: false
    }).then(function () {
      $.ajax({
        url: "/administrator/activate/" + id,
        method: "POST",
        success: function(result){
          swal({
            title: "Successfully Activated",
            type: "success",
            confirmButtonText: "Tamam",
          }).then( function(){ location.reload() } );
        },
        error: function (xhr, ajaxOptions, thrownError) {
          ajaxError(xhr, ajaxOptions, thrownError);
        }
      });
    })
  });
</script>
<?php partial_view('footer') ?>
