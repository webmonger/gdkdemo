using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace EnvironmentalSensorDemo.Cp.Core
{
    public class IDPair
    {
        private long _id;
        private String _guid;

        public IDPair(long id, String guid)
        {
            // TBD: Validation?
            this._id = id;
            this._guid = guid;
        }

        public long id()
        {
            return _id;
        }

        public string guid()
        {
            return _guid;
        }

        public int hashCode()
        {
            int hashId = _id == null ? 0 : _id.GetHashCode();
            int hashGuid = _guid == null ? 0 : _guid.GetHashCode();
            return (hashId + hashGuid)*hashGuid + hashId;
        }

        public override string ToString()
        {
            return "(" + _id + ", " + _guid + ")";
        }

    }
}