<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html ng-app="qwilr">
  <head>
    <title>Qwilr</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/flatNotify-0.1.css">
    <link rel="stylesheet" href="/css/style.css">
  </head>

  <body>
    <div class="container" style="margin-top: 20px">
      <g:if test="${loggedIn}">
        <div class="row">
          <a class="pull-right btn btn-default" href="/auth/logout">Logout</a>
          <span class="info">Hello ${userFirstName}</span>
        </div>
        <div class="row" ng-view></div>
      </g:if>
      <g:else>
        <div class="row" style="text-align: center; margin-top: 200px">
          <a class="btn btn-primary" href="/auth/google/login">Login with Google</a>
        </div>
      </g:else>
    </div>
  </body>

  <script src="/js/jquery.min.js"></script>
  <script src="/js/bootstrap.min.js"></script>

  <script src="/js/angular.min.js"></script>
  <script src="/js/angular-route.min.js"></script>
  <script src="/js/ui-bootstrap-tpls.min.js"></script>

  <script src="/js/classie.js"></script>
  <script src="/js/flatNotify-0.1.js"></script>

  <script src="/js/app.js"></script>
</html>