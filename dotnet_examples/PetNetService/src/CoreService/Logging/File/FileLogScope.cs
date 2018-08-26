using System;
using System.Threading;

namespace CoreService.Logging.File
{
    public class FileLogScope
    {
        private static readonly AsyncLocal<FileLogScope> _value = new AsyncLocal<FileLogScope>();
        private readonly object _state;

        internal FileLogScope(object state)
        {
            _state = state;
        }

        public FileLogScope Parent { get; private set; }

        public static FileLogScope Current
        {
            get => _value.Value;
            set => _value.Value = value;
        }

        public static IDisposable Push(object state)
        {
            FileLogScope current = Current;
            Current = new FileLogScope(state);
            Current.Parent = current;
            return new DisposableScope();
        }

        public override string ToString() => _state?.ToString();

        private class DisposableScope : IDisposable
        {
            public void Dispose() => Current = Current.Parent;
        }
    }
}
