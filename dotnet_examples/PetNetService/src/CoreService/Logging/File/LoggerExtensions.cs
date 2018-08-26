using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace CoreService.Logging.File
{
    public static class LoggerExtensions
    {
        public static ILoggingBuilder AddFile(this ILoggingBuilder builder, FileLogSettings settings)
        {
            builder.Services.AddSingleton<ILoggerProvider>(new FileLoggerProvider(settings));
            return builder;
        }
    }
}
