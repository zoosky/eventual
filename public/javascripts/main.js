/*
 * Author: Sari Haj Hussein
 */
var app = angular.module("app", ["ngResource"])
	.constant("apiUrl", "http://localhost:9000\:9000/api") // to tell AngularJS that 9000 is not a dynamic parameter
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/views/main",
			controller: "AppCtrl"
		}).otherwise({
			redirectTo: "/"
		});
	}
	]).config([
	"$locationProvider", function($locationProvider) {
		return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and histoty API
	}
]);

// global controller
app.controller("AppCtrl", ["$scope", "$resource", "apiUrl", function($scope, $resource, apiUrl) {
	  var Celebrities = $resource(apiUrl + "/celebrities"); // a RESTful-capable resource object
	  $scope.celebrities = Celebrities.query();
}]);