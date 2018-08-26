using System.Diagnostics;
using CoreService;
using Microsoft.AspNetCore.Mvc;
using PetService.Web.Models;

namespace PetService.Web.Controllers
{
    [Route("")]
    public class HomeController : Controller
    {
        private readonly TelemetryService _telemetryService;

        public HomeController(TelemetryService telemetryService)
        {
            _telemetryService = telemetryService;
        }

        [HttpGet]
        public IActionResult Index()
        {
            _telemetryService.Collect();
            return View();
        }

        [HttpGet("about")]
        public IActionResult About()
        {
            ViewData["Message"] = "Your application description page.";

            return View();
        }

        [HttpGet("contact")]
        public IActionResult Contact()
        {
            ViewData["Message"] = "Your contact page.";

            return View();
        }

        [HttpGet("privacy")]
        public IActionResult Privacy()
        {
            return View();
        }

        [HttpGet("path-info")]
        public ActionResult PathInfo()
        {
            var info = new
            {
                Path = HttpContext.Request.Path.Value,
                PathBase = HttpContext.Request.PathBase.Value
            };
            return Json(info);
        }

        [HttpGet("error")]
        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
        }
    }
}