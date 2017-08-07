<?php
  $success = get_flash_message('success');
  $error = get_flash_message('error');
?>
<?php if ($success) : ?>
  <div class="alert alert-success alert-dismissable">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <?= $success ?>
  </div>
<?php endif; ?>
<?php if ($error) : ?>
  <div class="alert alert-danger alert-dismissable">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <?= $error ?>
  </div>
<?php endif; ?>
