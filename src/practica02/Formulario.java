package practica02;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

/**
 * @author Iván Martín Molina
 */
public class Formulario extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField usuario;
	private JTextField correo;
	private JFormattedTextField telefono;
	private JPasswordField contrasena;
	private JTextArea datosEnviados;
	private JToggleButton btnMostrar;
	private char ecoOriginal;
	private ImageIcon iconoOjo;
	private ImageIcon iconoOjoTachado;

	public Formulario() {
		super("Formulario Practica 03");

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		usuario = new JTextField(20);
		correo = new JTextField(20);
		telefono = crearTelefono();
		contrasena = new JPasswordField(20);
		datosEnviados = new JTextArea(6, 30);
		datosEnviados.setEditable(false);
		datosEnviados.setLineWrap(true);
		datosEnviados.setWrapStyleWord(true);

		ecoOriginal = contrasena.getEchoChar();

		iconoOjo = cargarIcono("/recursos/ojo.png");
		iconoOjoTachado = cargarIcono("/recursos/ojo_tachado.png");

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(panel);

		int fila = 0;
		agregarCampo(panel, "Usuario (&U):", 'U', usuario, fila++);
		agregarCampo(panel, "Correo (&C):", 'C', correo, fila++);
		agregarCampo(panel, "Teléfono (&T):", 'T', telefono, fila++);
		agregarFilaContrasena(panel, fila++);
		agregarAreaDatos(panel, fila++);
		agregarBotonEnviar(panel, fila);

		usuario.setToolTipText("De 1 a 6 caracteres: letras sin tildes y dígitos.");
		correo.setToolTipText("Ejemplo: ana@correo.es");
		telefono.setToolTipText("Exactamente 9 dígitos.");
		contrasena.setToolTipText("Al menos 6 caracteres. Usa una contraseña de prueba.");
		datosEnviados.setToolTipText("Muestra los datos copiados al pulsar Enviar.");
		btnMostrar.setToolTipText("Muestra u oculta la contraseña.");

		pack();
		setMinimumSize(getSize());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void agregarCampo(JPanel panel, String texto, char atajo, JTextField campo, int fila) {
		JLabel etiqueta = new JLabel(texto);
		etiqueta.setDisplayedMnemonic(atajo);
		etiqueta.setLabelFor(campo);

		GridBagConstraints cEtiqueta = new GridBagConstraints();
		cEtiqueta.gridx = 0;
		cEtiqueta.gridy = fila;
		cEtiqueta.anchor = GridBagConstraints.WEST;
		cEtiqueta.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta, cEtiqueta);

		GridBagConstraints cCampo = new GridBagConstraints();
		cCampo.gridx = 1;
		cCampo.gridy = fila;
		cCampo.gridwidth = 2;
		cCampo.fill = GridBagConstraints.HORIZONTAL;
		cCampo.weightx = 1.0;
		cCampo.insets = new Insets(5, 5, 5, 5);
		panel.add(campo, cCampo);

		campo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!e.isTemporary()) {
					validar(campo);
				}
			}
		});
	}

	private void agregarFilaContrasena(JPanel panel, int fila) {
		JLabel etiqueta = new JLabel("Contraseña (&P):");
		etiqueta.setDisplayedMnemonic('P');
		etiqueta.setLabelFor(contrasena);

		GridBagConstraints cEtiqueta = new GridBagConstraints();
		cEtiqueta.gridx = 0;
		cEtiqueta.gridy = fila;
		cEtiqueta.anchor = GridBagConstraints.WEST;
		cEtiqueta.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta, cEtiqueta);

		GridBagConstraints cCampo = new GridBagConstraints();
		cCampo.gridx = 1;
		cCampo.gridy = fila;
		cCampo.fill = GridBagConstraints.HORIZONTAL;
		cCampo.weightx = 1.0;
		cCampo.insets = new Insets(5, 5, 5, 5);
		panel.add(contrasena, cCampo);

		btnMostrar = new JToggleButton("Mostrar", iconoOjo);
		btnMostrar.setMnemonic('O');
		if (iconoOjo != null) {
			btnMostrar.setIcon(iconoOjo);
		}

		GridBagConstraints cBoton = new GridBagConstraints();
		cBoton.gridx = 2;
		cBoton.gridy = fila;
		cBoton.insets = new Insets(5, 5, 5, 5);
		panel.add(btnMostrar, cBoton);

		contrasena.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!e.isTemporary()) {
					validar(contrasena);
				}
			}
		});

		btnMostrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alternarContrasena();
			}
		});
	}

	private void agregarAreaDatos(JPanel panel, int fila) {
		JLabel etiqueta = new JLabel("Datos enviados (&D):");
		etiqueta.setDisplayedMnemonic('D');
		etiqueta.setLabelFor(datosEnviados);

		GridBagConstraints cEtiqueta = new GridBagConstraints();
		cEtiqueta.gridx = 0;
		cEtiqueta.gridy = fila;
		cEtiqueta.anchor = GridBagConstraints.NORTHWEST;
		cEtiqueta.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta, cEtiqueta);

		JScrollPane scroll = new JScrollPane(datosEnviados);
		scroll.setPreferredSize(new Dimension(300, 120));

		GridBagConstraints cArea = new GridBagConstraints();
		cArea.gridx = 1;
		cArea.gridy = fila;
		cArea.gridwidth = 2;
		cArea.fill = GridBagConstraints.BOTH;
		cArea.weightx = 1.0;
		cArea.weighty = 1.0;
		cArea.insets = new Insets(5, 5, 5, 5);
		panel.add(scroll, cArea);
	}

	private void agregarBotonEnviar(JPanel panel, int fila) {
		JButton enviar = new JButton("Enviar");
		enviar.setMnemonic('V');
		enviar.setToolTipText("Valida los campos y copia los datos al área.");

		GridBagConstraints cBoton = new GridBagConstraints();
		cBoton.gridx = 1;
		cBoton.gridy = fila;
		cBoton.gridwidth = 2;
		cBoton.anchor = GridBagConstraints.WEST;
		cBoton.insets = new Insets(10, 5, 5, 5);
		panel.add(enviar, cBoton);

		enviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enviarFormulario();
			}
		});
	}

	private void alternarContrasena() {
		if (btnMostrar.isSelected()) {
			contrasena.setEchoChar((char) 0);
			btnMostrar.setText("Ocultar");
			if (iconoOjoTachado != null) {
				btnMostrar.setIcon(iconoOjoTachado);
			}
		} else {
			contrasena.setEchoChar(ecoOriginal);
			btnMostrar.setText("Mostrar");
			if (iconoOjo != null) {
				btnMostrar.setIcon(iconoOjo);
			}
		}
	}

	private void enviarFormulario() {
		boolean usuarioOk = validar(usuario);
		boolean correoOk = validar(correo);
		boolean telefonoOk = validar(telefono);
		boolean contrasenaOk = validar(contrasena);

		String texto = "Usuario: " + usuario.getText() + "\n"
				+ "Correo: " + correo.getText() + "\n"
				+ "Teléfono: " + telefono.getText();
		datosEnviados.setText(texto);

		if (usuarioOk && correoOk && telefonoOk && contrasenaOk) {
			JOptionPane.showMessageDialog(this,
					"Todos los datos son correctos.",
					"Envío correcto",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this,
					"Hay campos que deben revisarse.",
					"Revisa el formulario",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private boolean validar(JTextField campo) {
		boolean correcto = false;

		if (campo == usuario) {
			correcto = usuario.getText().matches("[a-zA-Z0-9]{1,6}");
		} else if (campo == correo) {
			correcto = correo.getText().trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");
		} else if (campo == telefono) {
			correcto = telefono.getText().matches("[0-9]{9}");
		} else if (campo == contrasena) {
			char[] caracteres = contrasena.getPassword();
			correcto = caracteres.length >= 6;
			Arrays.fill(caracteres, '\0');
		}

		campo.setBorder(BorderFactory.createLineBorder(
				correcto ? new Color(0, 128, 0) : Color.RED, 2));
		return correcto;
	}

	private JFormattedTextField crearTelefono() {
		try {
			MaskFormatter mascara = new MaskFormatter("#########");
			mascara.setPlaceholderCharacter('_');
			JFormattedTextField campo = new JFormattedTextField(mascara);
			campo.setColumns(20);
			campo.setFocusLostBehavior(JFormattedTextField.PERSIST);
			return campo;
		} catch (ParseException e) {
			throw new IllegalStateException("Máscara de teléfono incorrecta", e);
		}
	}

	private ImageIcon cargarIcono(String ruta) {
		URL url = getClass().getResource(ruta);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

}
