using System.Security.Cryptography;
using System.Text;

namespace BetaGo.Server.Utilities
{
    public class StringUtils
    {
        public static string SecureRandomString(int maxSize)
        {
            char[] chars = new char[62];
            chars = 
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".ToCharArray();
            byte[] data = new byte[1];
            using (RandomNumberGenerator prng = RandomNumberGenerator.Create())
            {
                prng.GetBytes(data);
                data = new byte[maxSize];
                prng.GetBytes(data);
            }
            var result = new StringBuilder(maxSize);
            foreach (byte b in data)
            {
                result.Append(chars[b % (chars.Length)]);
            }
            return result.ToString();
        }
    }
}