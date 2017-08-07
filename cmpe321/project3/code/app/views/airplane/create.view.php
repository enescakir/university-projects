<?php partial_view('header') ?>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">Create an Airlane</div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" method="POST" action="\airplane">
                        <div class="form-group">
                            <label for="model" class="col-md-4 control-label">Model</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" name="model" required autofocus>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="age" class="col-md-4 control-label">Age</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="age" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="row" class="col-md-4 control-label"># of Seat Rows</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="row" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="column" class="col-md-4 control-label"># of Seat Columns</label>
                            <div class="col-md-6">
                                <input type="number" class="form-control" name="column" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-6 col-md-offset-4">
                                <button type="submit" class="btn btn-primary">
                                    Save
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
