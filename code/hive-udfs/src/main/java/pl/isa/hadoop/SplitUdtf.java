package pl.isa.hadoop;

import com.google.common.collect.Lists;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;

import java.util.List;

public class SplitUdtf extends GenericUDTF {
        StringObjectInspector soi;
        public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
                soi = (StringObjectInspector)argOIs[0];

                List<ObjectInspector> ois = Lists.newArrayList();
                ois.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
                ois.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
                List<String> fields = Lists.newArrayList("ip", "method");

                return ObjectInspectorFactory.getStandardStructObjectInspector(fields, ois);
        }

        public void process(Object[] objects) throws HiveException {
                String field1 = soi.getPrimitiveJavaObject(objects[0]);
                String field2 = soi.getPrimitiveJavaObject(objects[1]);

                String[] field1Parts = field1.split("\\s+");
                String[] field2Parts = field2.split("\\s+");


                for(int i = 0 ; i < field1Parts.length ; i++) {
                    if(i < field2Parts.length) {
                            forward(new Object[]{field1Parts[i], field2Parts[i]});
                    }
                }
        }

        public void close() throws HiveException {

        }
}
