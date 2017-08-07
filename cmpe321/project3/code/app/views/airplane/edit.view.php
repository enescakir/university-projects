<?php partial_view('header') ?>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">Update the Airlane</div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" method="POST" action="\airplane\update\<?= $airplane->id ?>">
                        <div class="form-group">
                            <label for="model" class="col-md-4 control-label">Model</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" name="model" value="<?= $airplane->model ?>" required autofocus>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="age" class="col-md-4 control-label">Age</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="age" value="<?= $airplane->age ?>" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="row" class="col-md-4 control-label"># of Seat Rows</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="row" value="<?= $airplane->horizantal ?>" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="column" class="col-md-4 control-label"># of Seat Columns</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="column" value="<?= $airplane->vertical ?>" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-6 col-md-offset-4">
                                <button type="submit" class="btn btn-primary">
                                    Update
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
  <?php partial_view('scripts') ?>
  <?php partial_view('footer') ?>
