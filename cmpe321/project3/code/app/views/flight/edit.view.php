<?php partial_view('header') ?>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">Update an Flight</div>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" method="POST" action="\flight\update\<?= $flight->id ?>">
                        <div class="form-group">
                            <label for="number" class="col-md-4 control-label">Flight Number</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" name="number" value="<?= $flight->number ?>" required autofocus>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="departured_at" class="col-md-4 control-label">Departure Time</label>
                            <div class="col-md-6">
                                <input type="text" class="datetime-picker form-control" name="departured_at" value="<?= date("d/m/Y H:i", strtotime($flight->departured_at)) ?>" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="departure_id" class="col-md-4 control-label">Departure Airport</label>
                            <div class="col-md-6">
                              <select class="select2 form-control" name="departure_id" required>
                                <?php foreach ($airports as $airport) : ?>
                                  <option value="<?= $airport->id ?>"
                                    <?= $flight->departure_id == $airport->id ? "selected" : "" ?>
                                    ><?= $airport->name ?></option>
                                <?php endforeach; ?>
                              </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="destination_id" class="col-md-4 control-label">Destination Airport</label>
                            <div class="col-md-6">
                              <select class="select2 form-control" name="destination_id" required>
                                <?php foreach ($airports as $airport) : ?>
                                  <option value="<?= $airport->id ?>"
                                    <?= $flight->destination_id == $airport->id ? "selected" : "" ?>
                                    ><?= $airport->name ?></option>
                                <?php endforeach; ?>
                              </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pilot_id" class="col-md-4 control-label">Pilot</label>
                            <div class="col-md-6">
                              <select class="select2 form-control" name="pilot_id" required>
                                <?php foreach ($pilots as $pilot) : ?>
                                  <option value="<?= $pilot->id ?>"
                                    <?= $flight->pilot_id == $pilot->id ? "selected" : "" ?>
                                    ><?= $pilot->name ?></option>
                                <?php endforeach; ?>
                              </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="airplane_id" class="col-md-4 control-label">Airplane</label>
                            <div class="col-md-6">
                              <select class="select2 form-control" name="airplane_id" required>
                                <?php foreach ($airplanes as $airplane) : ?>
                                  <option value="<?= $airplane->id ?>"
                                    <?= $flight->airplane_id == $airplane->id ? "selected" : "" ?>
                                    ><?= $airplane->model ?></option>
                                <?php endforeach; ?>
                              </select>
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
