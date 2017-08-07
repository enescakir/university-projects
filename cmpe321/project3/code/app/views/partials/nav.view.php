<div class="collapse navbar-collapse" id="app-navbar-collapse">
  <!-- Left Side Of Navbar -->
  <ul class="nav navbar-nav">
    &nbsp;
  </ul>
  <ul class="nav navbar-nav navbar-left">
    <?php if (\Core\Auth::check('administrator')) : ?>
      <li><a href="/administrator">Administrators</a></li>
      <li><a href="/employee">Employees</a></li>
      <li><a href="/airport">Airports</a></li>
      <li><a href="/airplane">Airplanes</a></li>
      <li><a href="/pilot">Pilots</a></li>
      <li><a href="/flight">Flights</a></li>
    <?php elseif (\Core\Auth::check('employee')) : ?>
      <li><a href="/customer">Customers</a></li>
      <li><a href="/reservation">Reservations</a></li>
    <?php endif; ?>
  </ul>

  <!-- Right Side Of Navbar -->
  <ul class="nav navbar-nav navbar-right">
    <?php if (\Core\Auth::check()) : ?>
      <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
        <?= \Core\Auth::user()->name ?> <span class="caret"></span>
        </a>

        <ul class="dropdown-menu" role="menu">
          <li><a href="/logout"> Çıkış</a></li>
        </ul>
      </li>
    <?php else : ?>
      <li><a href="/login">Login</a></li>
      <li><a href="/register">Register</a></li>
    <?php endif; ?>
  </ul>
</div>
