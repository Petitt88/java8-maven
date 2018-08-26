using Xunit;
using Xunit.Sdk;

namespace CoreService.Tests
{
    public class CoreServiceTests
    {
        [Fact]
        public void PassingTest()
        {
            Assert.Equal(4, Add(2, 2));
        }

        [Fact]
        public void FailingTest()
        {
            Assert.Throws<EqualException>(() => Assert.Equal(5, Add(2, 2)));
        }

        [Theory]
        [InlineData(3)]
        [InlineData(5)]
        [InlineData(7)]
        public void MyFirstTheory(int value)
        {
            Assert.True(IsOdd(value));

            bool IsOdd(int val) => val % 2 == 1;
        }

        private int Add(int x, int y) => x + y;
    }
}