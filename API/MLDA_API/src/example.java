import mlda.base.MLDataMetric;
import mlda.labelsRelation.SCUMBLE;
import mulan.data.InvalidDataFormatException;
import mulan.data.MultiLabelInstances;

public class example {

	public static void main(String[] args) throws InvalidDataFormatException {
		SCUMBLE scumble = new SCUMBLE();
		MultiLabelInstances mlData = new MultiLabelInstances("birds.arff", "birds.xml");
		
		double val = scumble.calculate(mlData);
		System.out.println("SCUMBLE: " + val);
	}
}
