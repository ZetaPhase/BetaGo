using LiteDB;

namespace BetaGo.Server.Services.Database
{
    /// <summary>
    /// A service that contains global database table info, and provides access to the LiteDB store
    /// </summary>
    public static class DatabaseAccessService
    {
        public static string UsersCollectionDatabaseKey => "Users";

        private static LiteDatabase _dbInstance;

        public static LiteDatabase OpenOrCreateDefault()
        {
            if (_dbInstance == null)
            {
                _dbInstance = new LiteDatabase("betago_srv.lidb");
            }
            return _dbInstance;
        }
    }
}