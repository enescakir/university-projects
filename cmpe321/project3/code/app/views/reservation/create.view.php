<?php partial_view('header') ?>

<style>
  .plane {
    height: auto;
    width: 600px;
    overflow: hidden;
  }

  .plane-body {
    width:100%;
    overflow: hidden;
  }

  .plane-middle {
    float: left;
    margin: auto;
  }

  .wing {
    width:221px;
    float: left;
    position: relative;
    transform: translateY(150%);
  }

  .left-wing {
    text-align: right;
    margin-right: -7px;
  }

  .right-wing {
    text-align: left;
    margin-left: -7px;
  }
  .wing-img {
    position: absolute;
    margin: auto;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .seat-row {
    background-image:url('<?= asset('media/plane/plane_floor.png') ?>');
    background-repeat:no-repeat;
    background-size:contain;
    background-position:center;
    height: 41px;
    width: 100%;
    padding: 6px 10px 5px 10px;
    text-align: center;
    font-size: 18px;
    line-height: 1.5;
  }

  .seat {
    height: 30px;
    width: 30px;
    border-style: solid;
    border-width: 2px;
  }

  .seat-place {
    background-color: transparent;
    border-width: 0px;
    text-align: center;
    font-size: 20px;
    font-weight: bold;
  }

  .seat-left {
    float: left;
  }
  .seat-right {
    float: right;
  }
  .seat-half-left {
    width: 42%;
    float: left;
  }
  .seat-half-right {
    width: 42%;
    float: right;
  }

  .seat-selected {
    background-color: green;
  }

  .seat-empty {
    background-color: grey;
  }

  .seat-full {
    background-color: red;
  }

  .plane-back > img {
    margin: auto;
  }

</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">Make a Reservation</div>
                <div class="panel-body">
                    <div class="row">
                      <div class="col-md-6">
                        <div class="plane">
                          <div class="plane-body">
                            <div class="wing left-wing">
                              <img class="img-responsive wing-image" align="right" valign="center" src="<?= asset('media/plane/plane_wing_left.png') ?>">
                            </div>
                            <div class="plane-middle">
                              <img class="img-responsive" src="<?= asset('media/plane/plane_top.png') ?>">
                              <div class="seat-row">
                                <div class="seat-half-left">
                                  <div class="seat seat-place seat-left">A</div>
                                  <div class="seat seat-place seat-right">B</div>
                                </div>
                                <div class="seat-half-right">
                                  <div class="seat seat-place seat-left">C</div>
                                  <div class="seat seat-place seat-right">D</div>
                                </div>
                              </div>
                              <?php for ($row = 1; $row <= $airplane->horizantal; $row++) : ?>
                                <div class="seat-row">
                                  <?= $row ?>
                                  <div class="seat-half-left">
                                    <div class="seat <?= in_array("A" . $row, $full_seats) ? "seat-full" : "seat-empty" ?> seat-left" column="A" row="<?= $row ?>"></div>
                                    <div class="seat  <?= in_array("B" . $row, $full_seats) ? "seat-full" : "seat-empty" ?> seat-right" column="B" row="<?= $row ?>"></div>
                                  </div>
                                  <div class="seat-half-right">
                                    <div class="seat  <?= in_array("C" . $row, $full_seats) ? "seat-full" : "seat-empty" ?> seat-left" column="C" row="<?= $row ?>"></div>
                                    <div class="seat  <?= in_array("D" . $row, $full_seats) ? "seat-full" : "seat-empty" ?> seat-right" column="D" row="<?= $row ?>"></div>
                                  </div>
                                </div>
                              <?php endfor; ?>
                            </div>
                            <div class="wing right-wing">
                              <img class="img-responsive wing-image" align="left" src="<?= asset('media/plane/plane_wing_right.png') ?>">
                            </div>
                          </div>
                          <div class="plane-back">
                            <img class="img-responsive" src="<?= asset('media/plane/plane_bottom.png') ?>">
                          </div>
                        </div>
                      </div>
                      <div class="col-md-6">
                        <form class="form-horizontal" role="form" method="POST" action="\reservation">
                            <div class="form-group">
                                <label for="name" class="col-md-4 control-label">Customer Name</label>
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="name" required autofocus>
                                </div>
                            </div>
                            <h2 class="text-center"><strong>Seat: </strong> <br> <span id="seat-text">Select an seat</span></h2>
                            <br>
                            <div class="form-group text-center">
                              <button type="submit" class="btn btn-primary">
                                  Reserve it
                              </button>
                            </div>
                            <input type="hidden" name="flight_id" value="<?= $flight->id ?>" required>
                            <input id="seat-input" type="hidden" name="seat" required>
                        </form>
                      </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<?php partial_view('scripts') ?>
<!-- SEAT FUNCTIONS -->
<script type="text/javascript">
$('.seat-empty').on('click', function (e) {
  var column = $(this).attr('column');
  var row = $(this).attr('row');

  $('.seat-selected').each(function() {
    $(this).removeClass("seat-selected");
    $(this).addClass("seat-empty");
  });
  $(this).removeClass("seat-empty");
  $(this).addClass("seat-selected");
  $("#seat-text").html(column + row);
  $("#seat-input").val(column + row);
});
</script>
<?php partial_view('footer') ?>
