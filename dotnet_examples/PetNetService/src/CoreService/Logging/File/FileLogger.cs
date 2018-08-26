using System;
using Microsoft.Extensions.Logging;

namespace CoreService.Logging.File
{
    public class FileLogger : ILogger
    {
        private readonly FileLogSettings _settings;
        private readonly string _categoryName;
        private static object syncObject = new object();

        public FileLogger(FileLogSettings settings, string categoryName)
        {
            _settings = settings;
            _categoryName = categoryName;
        }

        public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception exception, Func<TState, Exception, string> formatter)
        {
            var message = $"{logLevel}: {_categoryName} {eventId.Id} - {formatter(state, exception)}{Environment.NewLine}";
            lock (syncObject)
            {
                System.IO.File.AppendAllText(_settings.FilePath, message);
            }
        }

        // gets filtered by LoggerFactory rules
        public bool IsEnabled(LogLevel logLevel) => true;

        public IDisposable BeginScope<TState>(TState state)
        {
            if (state == null) throw new ArgumentNullException(nameof(state));
            return FileLogScope.Push(state);
        }
    }
}