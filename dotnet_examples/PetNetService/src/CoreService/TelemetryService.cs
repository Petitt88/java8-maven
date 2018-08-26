using Microsoft.Extensions.Logging;

namespace CoreService
{
    public class TelemetryService
    {
        private readonly ILogger<TelemetryService> _logger;

        public TelemetryService(ILogger<TelemetryService> logger)
        {
            _logger = logger;
        }

        public void Collect()
        {
            _logger.LogInformation("Data collected");
        }
    }
}