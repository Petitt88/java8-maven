using Microsoft.Extensions.Logging;

namespace CoreService.Logging.File
{
    public class FileLoggerProvider : ILoggerProvider
    {
        private readonly FileLogSettings _settings;

        public FileLoggerProvider(FileLogSettings settings)
        {
            _settings = settings;
            if (!System.IO.File.Exists(_settings.FilePath))
            {
                System.IO.File.Create(_settings.FilePath);
            }
        }

        public ILogger CreateLogger(string categoryName) => new FileLogger(_settings, categoryName);

        public void Dispose()
        {
        }
    }
}